package com.soft.mobilele.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.soft.mobilele.model.enumerated.UserRoleEnum;
import com.soft.mobilele.model.user.MobileleUserDetails;
import com.soft.mobilele.service.UserRoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.mail.internet.MimeMessage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserUserRegistrationControllerGreenMailTest_IT {

    @Value("${mail.port}")
    private Integer mailPort;

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.username}")
    private String mailUsername;

    @Value("${mail.password}")
    private String mailPassword;

    private GreenMail greenMail;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserDetailsService mockUserDetailsService;

    @MockBean
    private UserRoleService mockUserRoleService;

    @BeforeEach
    void setUp() {
        final String protocol = "smtp";

        greenMail = new GreenMail(new ServerSetup(mailPort, mailHost, protocol));
        greenMail.start();
        greenMail.setUser(mailUsername, mailPassword);
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void testRegistration() throws Exception {
        // arrange
        MultiValueMap<String, String> keyValueParams = new LinkedMultiValueMap<>();
        keyValueParams.add("username", "anna2");
        keyValueParams.add("email", "anna2@example.com");
        keyValueParams.add("firstName", "Anna");
        keyValueParams.add("lastName", "Petrova");
        keyValueParams.add("password", "top-secret");
        keyValueParams.add("confirmPassword", "top-secret");

        final UserDetails currUserDetails = new MobileleUserDetails()
                .setUsername("anna2")
                .setEmail("anna2@example.com")
                .setFirstName("Anna")
                .setLastName("Petrova")
                .setPassword("HASHED#top-secret#HASHED")
                .setAuthorities(List.of(new SimpleGrantedAuthority(UserRoleEnum.USER.name())));

        when(mockUserDetailsService.loadUserByUsername("anna2"))
                .thenReturn(currUserDetails);
        // EO: arrange

        // act
        mockMvc.perform(post("/users/register")
                        .params(keyValueParams)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:registration-success"))
                .andExpect(redirectedUrl("registration-success"));

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        // EO: act

        // assert
        assertEquals(1, receivedMessages.length, "Expected only 1 message received");

        MimeMessage welcomeMessage = receivedMessages[0];
        assertTrue(welcomeMessage.getContent().toString().contains("Anna Petrova"),
                "Expected message to contain user full name");
        // EO: assert
    }
}
