package ua.kazo.dentalacademy.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AWSProperties {

    private String accessKeyId;
    private String secretKey;
    private String region;
    private String bucketName;

}
