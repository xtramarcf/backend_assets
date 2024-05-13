package de.fortmeier.asset_management.iam.config;

import de.fortmeier.asset_management.iam.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration for spring authentication process.
 */
@Configuration
@RequiredArgsConstructor
public class IamConfig {

    private final UserService userService;

    /**
     * @return an object of the UserDetailsService with the username.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    /**
     * Defines a BCryptPasswordEncoder and adds it to the core container.
     * @return the encoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Creates and configures the authenticationProvider. Adds it to the core container.
     * @return the provider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    /**
     * Creates the Authentication Manager and adds it to the core container
     * @param config injects the configuration.
     * @return the Authentication Manager.
     * @throws Exception for error.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
