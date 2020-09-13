package ua.kazo.dentalacademy.service.payment.processor;

import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentProcessorHolder {

    private final static Map<PaymentProvider, PaymentProcessor> map = new HashMap<>();

    public PaymentProcessorHolder(LiqPay_ liqPay, Fondy fondy, Portmone portmone) {
        map.put(PaymentProvider.LIQPAY, liqPay);
        map.put(PaymentProvider.FONDY, fondy);
        map.put(PaymentProvider.PORTMONE, portmone);
    }

    public static PaymentProcessor get(PaymentProvider provider) {
        return map.get(provider);
    }

}
