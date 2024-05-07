package de.fortmeier.asset_management.iam;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.fortmeier.asset_management.iam.auth.AuthRequest;
import de.fortmeier.asset_management.iam.auth.AuthResponse;
import de.fortmeier.asset_management.iam.auth.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Transactional
class AuthTest {

    @Autowired
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Value("${key.admin-password}")
    String adminPassword;

    @Test
    @Order(1)
    void authenticateTest() {

        Assertions.assertDoesNotThrow(() ->
                authService.authenticate(new AuthRequest(
                                "Admin", adminPassword
                        )
                )
        );
    }


    @Test
    @Order(2)
    void authenticateRequestTest() {

        AuthRequest request = new AuthRequest(
                "Admin", adminPassword
        );
        try {

            ResultActions resultActions = this.mockMvc.perform(post("/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(
                            status().isOk()
                    );

            MvcResult mvcResult = resultActions.andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString();
            AuthResponse authResponse = objectMapper.readValue(contentAsString, AuthResponse.class);

            assertNotNull(authResponse.accessToken());

        } catch (Exception e) {
            Assertions.fail(e);
        }

    }


}
