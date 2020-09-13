package ua.kazo.dentalacademy.config.payment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;

@Getter
@Setter
@ConfigurationProperties("payment")
public class PaymentProperties {

    private PaymentProvider provider;
    private String orderPrefix;
    private Long paymentTime;
    private String currency;
    private String callbackHost;
    @NestedConfigurationProperty
    private LiqPayProperties liqpay;
    @NestedConfigurationProperty
    private FondyProperties fondy;
    @NestedConfigurationProperty
    private PortmoneProperties portmone;

}
