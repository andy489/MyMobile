package com.mymobile.web;

import com.mymobile.model.entity.OfferEntity;
import com.mymobile.model.entity.UserEntity;
import com.mymobile.testutils.OfferTestDataUtil;
import com.mymobile.testutils.UserTestDataUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTest_IT {

    private static final String TEST_USER1_USERNAME = "user1";
    private static final String TEST_USER2_USERNAME = "user2";
    private static final String TEST_ADMIN_USERNAME = "admin";

    @Autowired
    private UserTestDataUtil userTestDataUtil;

    @Autowired
    private OfferTestDataUtil offerTestDataUtil;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        offerTestDataUtil.cleanUp();
        userTestDataUtil.cleanUp();
    }

    @AfterEach
    void tearDown() {
        offerTestDataUtil.cleanUp();
        userTestDataUtil.cleanUp();
    }

    @Test
    void testAnonymousDeletionFails() throws Exception {
        UserEntity seller = userTestDataUtil.createTestUser(TEST_USER1_USERNAME);
        OfferEntity offerEntity = offerTestDataUtil.createTestOffer(seller);

        mockMvc.perform(
                        delete("/offers/{id}", offerEntity.getId())
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/users/login"));
    }

    @Test
    @WithMockUser(
            username = TEST_USER1_USERNAME,
            roles = {"USER"}
    )
    void testNonAdminUserOwnOffer() throws Exception {
        UserEntity seller = userTestDataUtil.createTestUser(TEST_USER1_USERNAME);
        OfferEntity offerEntity = offerTestDataUtil.createTestOffer(seller);

        mockMvc.perform(
                        delete("/offers/{id}", offerEntity.getId())
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/offers"));
    }

    @Test
    @WithMockUser(
            username = TEST_USER2_USERNAME,
            roles = {"USER"}
    )
    void testNonAdminUserNotOwnOffer() throws Exception {
        UserEntity seller = userTestDataUtil.createTestUser(TEST_USER1_USERNAME);
        userTestDataUtil.createTestUser(TEST_USER2_USERNAME);
        OfferEntity offerEntity = offerTestDataUtil.createTestOffer(seller);

        mockMvc.perform(
                        delete("/offers/{id}", offerEntity.getId())
                                .with(csrf())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = TEST_ADMIN_USERNAME,
            roles = {"ADMIN"}
    )
    void testAdminUserNotOwnOffer() throws Exception {
        UserEntity seller = userTestDataUtil.createTestUser(TEST_USER1_USERNAME);
        userTestDataUtil.createTestAdmin(TEST_ADMIN_USERNAME);
        OfferEntity offerEntity = offerTestDataUtil.createTestOffer(seller);

        mockMvc.perform(
                        delete("/offers/{id}", offerEntity.getId())
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/offers"));
    }

}
