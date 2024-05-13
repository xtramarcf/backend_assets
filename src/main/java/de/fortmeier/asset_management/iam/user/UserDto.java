package de.fortmeier.asset_management.iam.user;


/**
 * DTO for sending user information to the frontend.
 * @param firstName
 * @param lastName
 * @param userName
 * @param enabled
 */
public record UserDto(
        String firstName,
        String lastName,
        String userName,
        boolean enabled
) {
}
