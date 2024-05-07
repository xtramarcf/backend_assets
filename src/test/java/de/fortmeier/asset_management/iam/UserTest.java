package de.fortmeier.asset_management.iam;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.fortmeier.asset_management.iam.auth.AuthRequest;
import de.fortmeier.asset_management.iam.auth.AuthResponse;
import de.fortmeier.asset_management.iam.user.Role;
import de.fortmeier.asset_management.iam.user.User;
import de.fortmeier.asset_management.iam.user.UserService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Transactional
class UserTest {

    @Autowired
    UserService userService;

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

        userService.save(user);
        Assertions.assertTrue(userService.findByUserName(userName).isPresent());

        userService.enableUser(userName);
        User foundUser = userService.findByUserName(userName).orElseThrow();
        Assertions.assertTrue(foundUser.isEnabled());


        userService.deleteUser(userName);
        Assertions.assertTrue(userService.findByUserName(userName).isEmpty());
    }


    @Test
    @Order(2)
    void enableDeleteRequestTest() {

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

        userService.save(user);

        try {
            this.mockMvc.perform(post("/users/enable")
                            .param("userName", userName)
                            .header("Authorization", token))
                    .andExpect(
                            status().isOk()
                    );

        } catch (Exception e) {
            Assertions.fail(e);
        }

        Assertions.assertTrue(userService.findByUserName(userName).orElseThrow().isEnabled());


        try {
            this.mockMvc.perform(delete("/users/delete")
                            .param("userName", userName)
                            .header("Authorization", token))
                    .andExpect(
                            status().isOk()
                    );

        } catch (Exception e) {
            Assertions.fail(e);
        }

        Assertions.assertTrue(userService.findByUserName(userName).isEmpty());
    }


    String authenticate() throws Exception {
        AuthRequest request = new AuthRequest("Admin", adminPassword);

        ResultActions resultActions = this.mockMvc.perform(post("/auth/authenticate")
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
