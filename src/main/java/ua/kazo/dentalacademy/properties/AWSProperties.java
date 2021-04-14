package ua.kazo.dentalacademy.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("aws")
public class AWSProperties {

    private String accessKeyId;
    private String secretKey;
    private String region;
    private String bucketName;

}
