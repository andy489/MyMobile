package com.soft.mobilele.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.soft.mobilele.model.enumerated.UserRoleEnum;
import com.soft.mobilele.model.user.MobileleUserDetails;
import com.soft.mobilele.service.UserRoleService;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
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

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserRegistrationControllerMockBean_IT {

    @Autowired
    private MockMvc mockMvc;

    // @MockBean to mock an object that is present in the Spring application context.
    // It takes care of replacing the bean with what we want to simulate in our test.
    // When we communicate with external server like cloudinary, smtp server or etc.
    // @MockBean
    // private MailService mockMailService;

    @Value("${mail.port}")
    private Integer port;
    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;

    private GreenMail greenMail;

    @Mock
    private UserDetailsService mockUserDetailsService;

     @MockBean
     private UserRoleService mockUserRoleService;

    // @Captor
    // ArgumentCaptor<String> activationToken;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);
    }

    @AfterEach
    void tearDown() {
       greenMail.stop();
    }

    @Test
    void smokeTest() {
        assertNotNull(mockMvc, "Expect MockMvc to be autowired");
    }

    @Test
    void testRegistrationPageShown() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }

    @Test
    void testUserRegistration() throws Exception {
        // arrange
        final MultiValueMap<String, String> keyValueParams = new LinkedMultiValueMap<>();
        keyValueParams.add("username", "anna");
        keyValueParams.add("email", "anna@example.com");
        keyValueParams.add("firstName", "Anna");
        keyValueParams.add("lastName", "Petrova");
        keyValueParams.add("password", "top-secret");
        keyValueParams.add("confirmPassword", "top-secret");

        final UserDetails currUserDetails = new MobileleUserDetails()
                .setUsername("anna")
                .setEmail("anna@example.com")
                .setFirstName("Anna")
                .setLastName("Viktoria")
                .setPassword("HASHED#top-secret#HASHED")
                .setAuthorities(List.of(new SimpleGrantedAuthority(UserRoleEnum.USER.name())));

        when(mockUserDetailsService.loadUserByUsername("anna"))
                .thenReturn(currUserDetails);
        // EO: arrange

        // act
        mockMvc.perform(post("/users/register")
                        .params(keyValueParams)
                        .with(csrf())
                        .cookie(new Cookie("lang", Locale.GERMAN.getLanguage()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:registration-success"))
                .andExpect(redirectedUrl("registration-success"));

        greenMail.waitForIncomingEmail(1);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        // EO: act

        // asser
        assertEquals(1, receivedMessages.length);
        MimeMessage registrationMessage = receivedMessages[0];

        assertTrue(registrationMessage.getContent().toString().contains("Anna Petrova"));
        assertEquals(1, registrationMessage.getAllRecipients().length);
        assertEquals("anna@example.com", registrationMessage.getAllRecipients()[0].toString());
        // EO: assert

//        verify(mockMailService).sendRegistrationEmail(
//                eq("anna@example.com"),
//                eq("Anna Viktoria"),
//                eq(Locale.GERMAN),
//                activationToken.capture());

//        assertEquals(activationToken.getValue().length(), 20);

    }
}
