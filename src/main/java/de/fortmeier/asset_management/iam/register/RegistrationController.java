package de.fortmeier.asset_management.iam.register;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller with register request.
 */
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;


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
            registrationService.register(request);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

}
