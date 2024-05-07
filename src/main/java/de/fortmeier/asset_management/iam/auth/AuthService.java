package de.fortmeier.asset_management.iam.auth;

import de.fortmeier.asset_management.iam.config.JwtService;
import de.fortmeier.asset_management.iam.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public AuthResponse authenticate(AuthRequest request) {

        var user = userService.findByUserName(request.getUserName())
                .orElseThrow(IllegalStateException::new);

        if (!user.isEnabled()) throw new IllegalStateException("User not enabled");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken);
    }


}
