package de.fortmeier.asset_management.iam.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for methods to authenticate and log out users.
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    /**
     * Method for authenticating users.
     * @param request object with user credentials
     * @return the response object with jwt token.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {

        try {
            AuthResponse response = authService.authenticate(request);
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

}
