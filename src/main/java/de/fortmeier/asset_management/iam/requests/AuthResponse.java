package de.fortmeier.asset_management.iam.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication response with jwt.
 * @param accessToken is the jwt token.
 */

public record AuthResponse(
        @JsonProperty("access_token")
        String accessToken
) {
}
