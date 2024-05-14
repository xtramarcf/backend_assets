package de.fortmeier.asset_management.iam;

import de.fortmeier.asset_management.iam.requests.AuthRequest;
import de.fortmeier.asset_management.iam.requests.AuthResponse;
import de.fortmeier.asset_management.iam.requests.RegistrationRequest;
import de.fortmeier.asset_management.iam.requests.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller with all IAM operations.
 */
@RestController
@RequestMapping("/iam")
@RequiredArgsConstructor
public class IamController {

    private final IamService iamService;

    /**
     * Gets all user.
     *
     * @return the Response Entity with the status code and all found user in its body.
     */
    @GetMapping("/get-all-user")
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<User> users = iamService.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : users) {
            userDtoList.add(
                    new UserDto(
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUsername(),
                            user.isEnabled()
                    )
            );
        }

        return ResponseEntity.ok(userDtoList);
    }

    /**
     * Enables a registered user by the userName.
     *
     * @param userName Name of the user, which will be enabled.
     * @return the Response Entity with the status code.
     */
    @PostMapping("/enable-user")
    public ResponseEntity<String> enableUser(@RequestParam("userName") String userName) {

        try {
            iamService.enableUser(userName);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();

    }

    /**
     * Deletes a user by the userName.
     * @param userName Name of the user, which will be deleted.
     * @return a Response Entity with the status code.
     */
    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam("userName") String userName) {

        try {
            if (userName.equals("Admin"))
                return ResponseEntity.badRequest().build();
            iamService.deleteUser(userName);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }


    /**
     * Method for authenticating users.
     * @param request object with user credentials
     * @return the response object with jwt token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {

        try {
            AuthResponse response = iamService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException | LockedException e) {
            return ResponseEntity.badRequest().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Log out clears the spring security context.
     */
    @GetMapping("/logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }


    /**
     * Method for register as a user.
     * @param request with users credentials
     * @param result BindingResult from spring validation for validating the request.
     * @return a ResponseEntity with the http status.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid RegistrationRequest request,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            iamService.register(request);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

}
