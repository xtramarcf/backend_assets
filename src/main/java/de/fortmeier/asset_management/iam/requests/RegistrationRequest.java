package de.fortmeier.asset_management.iam.requests;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * RegistrationRequest-Object with validation.
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RegistrationRequest {

    @NotNull
    private String userName;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{10,}$")
    private String password;
}
