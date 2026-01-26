package com.mymobile.web.rest;


import com.mymobile.testutils.ExchangeRateTestDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ImportAutoConfiguration(exclude = OAuth2ClientAutoConfiguration.class)
class CurrencyRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExchangeRateTestDataUtil testData;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Test
    void testConvert() throws Exception {

        testData.createExchangeRate("EUR", BigDecimal.valueOf(0.54));

        this.mockMvc.perform(get("/api/currency/convert")
                        .param("target", "EUR")
                        .param("amount", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.amount", is(540d)));
    }
}