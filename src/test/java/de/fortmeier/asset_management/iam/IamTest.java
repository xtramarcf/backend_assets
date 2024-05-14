package de.fortmeier.asset_management.iam;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fortmeier.asset_management.iam.requests.AuthRequest;
import de.fortmeier.asset_management.iam.requests.AuthResponse;
import de.fortmeier.asset_management.iam.requests.RegistrationRequest;
import de.fortmeier.asset_management.iam.requests.UserDto;
import de.fortmeier.asset_management.iam.types.Role;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Transactional
class IamTest {

    @Autowired
    IamService iamService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Value("${key.admin-password}")
    String adminPassword;
    private static final String firstName = "john";
    private static final String lastName = "doe";
    private static final String userName = "johnDoe";
    private static final String password = "john1234";


    /**
     * Tests finding, enabling and deleting user.
     */
    @Test
    @Order(1)
    void saveFindEnableDeleteTest() {

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .role(Role.USER)
                .build();

        iamService.save(user);
        Assertions.assertTrue(iamService.findByUserName(userName).isPresent());

        iamService.enableUser(userName);
        User foundUser = iamService.findByUserName(userName).orElseThrow();
        Assertions.assertTrue(foundUser.isEnabled());


        iamService.deleteUser(userName);
        Assertions.assertTrue(iamService.findByUserName(userName).isEmpty());
    }


    /**
     * Tests enabling and deleting user by request.
     */
    @Test
    @Order(2)
    void findEnableDeleteRequestTest() {

        String token = null;
        try {
            token = authenticate();
        } catch (Exception e) {
            Assertions.fail(e);
        }

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .password(password)
                .role(Role.USER)
                .build();

        iamService.save(user);

        try {
            ResultActions resultActions = this.mockMvc.perform(get("/iam/get-all-user")
                            .param("userName", userName)
                            .header("Authorization", token))
                    .andExpect(
                            status().isOk()
                    );

            MvcResult result = resultActions.andReturn();
            String contentAsString = result.getResponse().getContentAsString();
            List<UserDto> userDtoList = objectMapper.readValue(contentAsString, new TypeReference<List<UserDto>>() {
            });

            Assertions.assertEquals(2, userDtoList.size());

        } catch (Exception e) {
            Assertions.fail(e);
        }


        try {
            this.mockMvc.perform(post("/iam/enable-user")
                            .param("userName", userName)
                            .header("Authorization", token))
                    .andExpect(
                            status().isOk()
                    );

            Assertions.assertTrue(iamService.findByUserName(userName).orElseThrow().isEnabled());
        } catch (Exception e) {
            Assertions.fail(e);
        }


        try {
            this.mockMvc.perform(delete("/iam/delete-user")
                            .param("userName", userName)
                            .header("Authorization", token))
                    .andExpect(
                            status().isOk()
                    );

            Assertions.assertTrue(iamService.findByUserName(userName).isEmpty());
        } catch (Exception e) {
            Assertions.fail(e);
        }


    }


    /**
     * Tests register Admin.
     */
    @Test
    @Order(3)
    void registerAdminTest() {

        iamService.registerAdmin();

        Assertions.assertEquals("ADMIN", iamService.findByUserName("Admin").orElseThrow().getRole().name());
    }


    /**
     * Tests register user.
     */
    @Test
    @Order(4)
    void registerTest() {
        iamService.register(new RegistrationRequest(
                userName,
                firstName,
                lastName,
                password
        ));

        User user = iamService.findByUserName(userName).orElseThrow();

        Assertions.assertEquals(userName, user.getUsername());
        Assertions.assertEquals("USER", user.getRole().name());
    }


    /**
     * Tests authentication.
     */
    @Test
    @Order(5)
    void authenticateTest() {

        Assertions.assertDoesNotThrow(() ->
                iamService.authenticate(new AuthRequest(
                                "Admin", adminPassword
                        )
                )
        );
    }


    /**
     * Tests authentication request.
     */
    @Test
    @Order(6)
    void authenticateRequestTest() {

        AuthRequest request = new AuthRequest(
                "Admin", adminPassword
        );
        try {

            ResultActions resultActions = this.mockMvc.perform(post("/iam/login")
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


    /**
     * Authenticates the admin.
     *
     * @return the authentication information.
     * @throws Exception if an error occurs.
     */
    String authenticate() throws Exception {
        AuthRequest request = new AuthRequest("Admin", adminPassword);

        ResultActions resultActions = this.mockMvc.perform(post("/iam/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(
                        status().isOk()
                );

        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(contentAsString, AuthResponse.class);

        return "Bearer " + authResponse.accessToken();
    }
}
