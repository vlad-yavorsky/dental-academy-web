package ua.kazo.dentalacademy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import ua.kazo.dentalacademy.properties.AppProperties;
import ua.kazo.dentalacademy.service.storage.S3StorageService;
import ua.kazo.dentalacademy.service.storage.StorageService;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final AppProperties appProperties;

    @Bean
    public Region region() {
        return Region.of(appProperties.getAws().getRegion());
    }

    @Bean
    public S3Client s3Client(Region region) {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials
                                .create(appProperties.getAws().getAccessKeyId(), appProperties.getAws().getSecretKey())))
                .region(region)
                .build();
    }

    @Bean
    public StorageService storageService() {
        return new S3StorageService(s3Client(region()), appProperties);
    }

}
