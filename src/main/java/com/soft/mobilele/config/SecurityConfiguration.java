package com.soft.mobilele.config;

import com.soft.mobilele.mapper.MapStructMapper;
import com.soft.mobilele.model.enumarated.UserRoleEnum;
import com.soft.mobilele.repository.UserRepository;
import com.soft.mobilele.service.MobileleUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder encode() {

//        final String SALT = "pepper";
//        final int ITERATIONS = (int) 2e5;
//        final int SALT_LENGTH = 0;

//        return new Pbkdf2PasswordEncoder(
//                SALT,
//                SALT_LENGTH,
//                ITERATIONS,
//                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
//        );


//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, MapStructMapper mapper) {
        return new MobileleUserDetailsService(userRepository, mapper);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http // .csrf(AbstractHttpConfigurer::disable)
                // defines which pages will be authorized
                .authorizeHttpRequests((auth) -> {
                    auth
                            // allow access to all static locations defined in StaticResourceLocation enum class (images, css, js, webjars, etc.)
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                            // the URLs below are available for all users - logged in and anonymous
                            .requestMatchers("/", "/index", "/users/login", "/users/register", "/users/login-error").permitAll()
                            // the URLs below are available only for moderators or admins
                            .requestMatchers("/pages/moderators").hasAnyRole(UserRoleEnum.MODERATOR.name(), UserRoleEnum.ADMIN.name())
                            // the URLs below are available only for admins
                            .requestMatchers("/pages/admins").hasRole(UserRoleEnum.ADMIN.name())
                            .requestMatchers("/brands/**").permitAll()
                            .requestMatchers("/offers/add").authenticated()
                            .requestMatchers("/offers/**").permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .formLogin(form -> {
                    form
                            .loginPage("/users/login")
                            .loginProcessingUrl("/users/login")
                            .failureForwardUrl("/users/login-error")
                            // where do we go after login (use true argument if you want to go there, otherwise go to previous page)
                            .defaultSuccessUrl("/" /*,true*/) // arg alwaysUse: true
                            // the names of the "username" and "password" input fields in the custom login form
                            .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                            .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                            .permitAll();
                })
                .logout(logout -> {
                    logout
                            .logoutUrl("/users/logout")
                            // go to home page after logout
                            .logoutSuccessUrl("/")
                            .clearAuthentication(true)
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll();
                })
                .securityContext(context -> {
                    context.securityContextRepository(securityContextRepository());
                })
                .build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }
}
