package com.soft.mobilele.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.soft.mobilele.service.UserRoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.mail.internet.MimeMessage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserUserRegistrationControllerGreenMail_IT {

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
        keyValueParams.add("username", "anna");
        keyValueParams.add("email", "anna@example.com");
        keyValueParams.add("firstName", "Anna");
        keyValueParams.add("lastName", "Viktoria");
        keyValueParams.add("password", "top-secret");
        keyValueParams.add("confirmPassword", "top-secret");

        // EO: arrange

        // act
        mockMvc.perform(post("/users/register")
                        .params(keyValueParams)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        // EO: act

        // assert
        assertEquals(1, receivedMessages.length, "Expected only 1 message received");

        MimeMessage welcomeMessage = receivedMessages[0];
        assertTrue(welcomeMessage.getContent().toString().contains("Anna Viktoria"),
                "Expected message to contain user full name");
        // EO: assert
    }
}
