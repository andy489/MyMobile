package com.mymobile.service.oauth;

import com.mymobile.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    public OAuthSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        if (authentication instanceof OAuth2AuthenticationToken token) {

            OAuth2User user = token.getPrincipal();

            String username = user.getAttribute("login");
            String email = token.getPrincipal().getAttribute("email");
            String fullName = token.getPrincipal().getAttribute("name");

            String firstName = null;
            String lastName = null;
            if (fullName != null) {
                String[] s = fullName.split("\\s+");

                firstName = s[0];
                if (s.length > 1) {
                    lastName = s[1];
                }
            }

            userService.createUserIfNotExist(username, email, firstName, lastName);
            authentication = userService.login(username);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
