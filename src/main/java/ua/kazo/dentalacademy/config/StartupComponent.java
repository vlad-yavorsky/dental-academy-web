package ua.kazo.dentalacademy.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.kazo.dentalacademy.service.storage.StorageService;

@Configuration
public class StartupComponent {

    @Bean
    public CommandLineRunner init(StorageService storageService) {
        return args -> storageService.init();
    }

}
