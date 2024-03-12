package com.mymobile.service;

import com.mymobile.model.user.MobileleUserDetails;
import com.mymobile.repository.UserRepository;
import com.mymobile.mapper.MapStructMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

// NOTE: This is not annotated as @Service, because we will return it as a bean,
// but the bean is already created in SecurityConfiguration class
public class MobileleUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final MapStructMapper mapper;

    public MobileleUserDetailsService(UserRepository userRepository, MapStructMapper mapper) {

        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MobileleUserDetails> mobileleUserDetails = userRepository.findByUsername(username)
                .map(u -> {
                    MobileleUserDetails userDetails = mapper.toUserDetails(u);

                    userDetails.setAuthorities(u.getUserRoles().stream()
                            .map(mapper::toGrantedAuthority)
                            .toList());

                    return userDetails;
                });

        return mobileleUserDetails
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

}
