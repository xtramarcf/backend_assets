package de.fortmeier.asset_management;

import de.fortmeier.asset_management.assets.service.AssetService;
import de.fortmeier.asset_management.iam.register.RegistrationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetManagementApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(
            RegistrationService registrationService,
            AssetService assetService
    ) {
        return args -> {
            registrationService.registerAdmin();
            assetService.saveExampleAsset();
        };
    }
}
