package ua.kazo.dentalacademy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LiqPayBean {

    private final LiqPayProperties liqPayProperties;

    @Bean
    public CustomLiqPay liqPay() {
        return new CustomLiqPay(liqPayProperties.getPublicKey(), liqPayProperties.getPrivateKey());
    }

}
