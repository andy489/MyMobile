package com.soft.mobilele.service;

import com.soft.mobilele.mapper.MapStructMapper;
import com.soft.mobilele.model.dto.UserRegistrationDTO;
import com.soft.mobilele.model.entity.UserEntity;
import com.soft.mobilele.model.enumarated.UserRoleEnum;
import com.soft.mobilele.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final MapStructMapper mapper;

    private final UserDetailsService userDetailsService;

    private final UserRoleService userRoleService;

    private final EmailService emailService;

//    private final String defaultAdminPassword;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder encoder,
            MapStructMapper mapper,
            UserDetailsService userDetailsService,
            UserRoleService userRoleService,
            EmailService emailService
//            ,@Value("{mobilele.admin.defaultpass}") String defaultAdminPassword
    ) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.mapper = mapper;
        this.userDetailsService = userDetailsService;
        this.userRoleService = userRoleService;
        this.emailService = emailService;
//        this.defaultAdminPassword = defaultAdminPassword;
    }

    public void registerAndLogin(UserRegistrationDTO userRegistrationDto,
                                 Locale localeResolver,
                                 Consumer<Authentication> successfulLoginProcessor) {

        UserEntity newUser = mapper.toEntity(userRegistrationDto);

        newUser.setPassword(encoder.encode(userRegistrationDto.getPassword()));
        newUser.addRole(userRoleService.getByUserRoleEnum(UserRoleEnum.USER));

        newUser = userRepository.save(newUser);

        try {
            // send email
            emailService.sendRegistrationEmail(newUser.getEmail(), newUser.getFullName(), localeResolver);
        } catch (MailSendException mse) {
            LOGGER.warn("Failed to send email to user: " + userRegistrationDto.getUsername());
            LOGGER.warn("SMTP server connection (MailHog) refused: " + mse.getLocalizedMessage());
//            throw new IllegalStateException();
        }

        // auto-login
        UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, // principal
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        successfulLoginProcessor.accept(authentication);
    }


    public Optional<UserEntity> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such user (getById)"));
    }
}
