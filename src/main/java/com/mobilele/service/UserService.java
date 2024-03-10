package com.mobilele.service;

import com.mobilele.model.event.UserRegisteredEvent;
import com.mobilele.repository.UserRepository;
import com.mobilele.mapper.MapStructMapper;
import com.mobilele.model.dto.UserRegistrationDto;
import com.mobilele.model.entity.UserEntity;
import com.mobilele.model.enumerated.UserRoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final MapStructMapper mapper;

    private final UserDetailsService userDetailsService;

    private final UserRoleService userRoleService;

    private final MailService mailService;

//    private final String defaultAdminPassword;

    private final ApplicationEventPublisher appEventPublisher;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder encoder,
            MapStructMapper mapper,
            UserDetailsService userDetailsService,
            UserRoleService userRoleService,
            MailService mailService,
            ApplicationEventPublisher appEventPublisher) {

        this.userRepository = userRepository;
        this.encoder = encoder;
        this.mapper = mapper;
        this.userDetailsService = userDetailsService;
        this.userRoleService = userRoleService;
        this.mailService = mailService;
        this.appEventPublisher = appEventPublisher;
    }

    public void registerAndLogin(UserRegistrationDto userRegistrationDto,
                                 Locale locale,
                                 Consumer<Authentication> successfulLoginProcessor) {

        UserEntity newUser = mapper.toEntity(userRegistrationDto);

        newUser.setPassword(encoder.encode(userRegistrationDto.getPassword()));
        newUser.addRole(userRoleService.getByUserRoleEnum(UserRoleEnum.USER));

        newUser = userRepository.save(newUser);

        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent("UserService",
                userRegistrationDto.getEmail(), userRegistrationDto.getUserFullName(), locale);

        appEventPublisher.publishEvent(userRegisteredEvent);

        // auto-login
        boolean autoLogin = false;
        if(autoLogin) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getUsername());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, // principal
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );

            successfulLoginProcessor.accept(authentication);
        }
    }


    public UserEntity getByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public UserEntity getByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such user (getById)"));
    }
}
