package ua.kazo.dentalacademy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("liqpay")
public class LiqPayProperties {

    private String privateKey;
    private String publicKey;
    private String callbackHost;
    private String orderPrefix;
    private Long paymentTime;

}
