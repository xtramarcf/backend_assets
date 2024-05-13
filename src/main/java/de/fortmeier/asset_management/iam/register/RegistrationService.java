package de.fortmeier.asset_management.iam.register;


import de.fortmeier.asset_management.iam.user.Role;
import de.fortmeier.asset_management.iam.user.User;
import de.fortmeier.asset_management.iam.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Service with registration business logic.
 */
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Value("${key.admin-password}")
    String adminPassword;

    /**
     * Registers a user by the given user credentials.
     *
     * @param request contains the user credentials.
     */
    public void register(RegistrationRequest request) {

        if (userService.findByUserName(request.getUserName()).isPresent()) {
            throw new IllegalStateException();
        }

        User user = User.builder()
                .userName(request.getUserName())
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .role(Role.USER)
                .password(encoder.encode(request.getPassword()))
                .build();

        userService.save(user);
    }


    /**
     * Registers the admin-user, if the user does not exist.
     */
    public void registerAdmin() {

        if (userService.findByUserName("Admin").isPresent()) return;

        User user = User.builder()
                .userName("Admin")
                .lastName("Admin")
                .firstName("Admin")
                .role(Role.ADMIN)
                .password(encoder.encode(adminPassword))
                .enabled(true)
                .build();

        userService.save(user);
    }
}
