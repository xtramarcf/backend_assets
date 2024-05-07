package de.fortmeier.asset_management.iam.user;

public record UserDto(
        String firstName,
        String lastName,
        String userName,
        boolean enabled
) {
}
