package ua.kazo.dentalacademy.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;
import ua.kazo.dentalacademy.properties.payment.FondyProperties;
import ua.kazo.dentalacademy.properties.payment.LiqPayProperties;
import ua.kazo.dentalacademy.properties.payment.PortmoneProperties;
import ua.kazo.dentalacademy.properties.payment.WayForPayProperties;

@Getter
@Setter
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
    @NestedConfigurationProperty
    private WayForPayProperties wayforpay;

}
