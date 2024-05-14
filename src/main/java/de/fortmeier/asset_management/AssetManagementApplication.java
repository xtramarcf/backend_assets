package de.fortmeier.asset_management;

import de.fortmeier.asset_management.assets.AssetService;
import de.fortmeier.asset_management.iam.IamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetManagementApplication.class, args);
    }


    /**
     * Defined CommandLineRunner will be executed automatically after the core container has been started.
     * Registers the admin and saves some example assets.
     * @param iamService injecting the registrationService.
     * @param assetService injecting the assetService.
     * @return the code, which will be executed.
     */
    @Bean
    public CommandLineRunner commandLineRunner(
            IamService iamService,
            AssetService assetService
    ) {
        return args -> {
            iamService.registerAdmin();
            assetService.saveExampleAsset();
        };
    }
}
