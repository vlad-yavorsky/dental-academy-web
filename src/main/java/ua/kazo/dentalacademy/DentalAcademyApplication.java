package ua.kazo.dentalacademy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import ua.kazo.dentalacademy.properties.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class DentalAcademyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DentalAcademyApplication.class, args);
    }

}
