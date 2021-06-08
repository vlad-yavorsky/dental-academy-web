package ua.kazo.dentalacademy.service.payment.processor;

import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;

import java.util.EnumMap;
import java.util.Map;

@Component
public class PaymentProcessorHolder {

    private static final Map<PaymentProvider, PaymentProcessor> map = new EnumMap<>(PaymentProvider.class);

    public PaymentProcessorHolder(LiqPayExt liqPay, Fondy fondy, Portmone portmone, WayForPay wayForPay) {
        map.put(PaymentProvider.LIQPAY, liqPay);
        map.put(PaymentProvider.FONDY, fondy);
        map.put(PaymentProvider.PORTMONE, portmone);
        map.put(PaymentProvider.WAYFORPAY, wayForPay);
    }

    public static PaymentProcessor get(PaymentProvider provider) {
        return map.get(provider);
    }

}
