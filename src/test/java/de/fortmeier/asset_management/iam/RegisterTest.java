package de.fortmeier.asset_management.iam;


import de.fortmeier.asset_management.iam.register.RegistrationRequest;
import de.fortmeier.asset_management.iam.register.RegistrationService;
import de.fortmeier.asset_management.iam.user.User;
import de.fortmeier.asset_management.iam.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class RegisterTest {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserService userService;

    private static final String firstName = "john";
    private static final String lastName = "doe";
    private static final String userName = "johnDoe";
    private static final String password = "john1234";

    @Test
    @Order(1)
    void registerAdminTest() {

        registrationService.registerAdmin();

        Assertions.assertEquals("ADMIN", userService.findByUserName("Admin").orElseThrow().getRole().name());
    }


    @Test
    @Order(2)
    void registerTest() {
        registrationService.register(new RegistrationRequest(
                userName,
                firstName,
                lastName,
                password
        ));

        User user = userService.findByUserName(userName).orElseThrow();

        Assertions.assertEquals(userName, user.getUsername());
        Assertions.assertEquals("USER", user.getRole().name());
    }
}
