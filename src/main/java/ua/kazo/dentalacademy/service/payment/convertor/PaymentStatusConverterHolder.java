package ua.kazo.dentalacademy.service.payment.convertor;

import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentStatusConverterHolder {

    private final static Map<PaymentProvider, PaymentStatusConverter> map = new HashMap<>();

    public PaymentStatusConverterHolder(LiqPayPaymentStatus liqPay, FondyPaymentStatus fondy, PortmonePaymentStatus portmone) {
        map.put(PaymentProvider.LIQPAY, liqPay);
        map.put(PaymentProvider.FONDY, fondy);
        map.put(PaymentProvider.PORTMONE, portmone);
    }

    public static PaymentStatusConverter get(PaymentProvider provider) {
        return map.get(provider);
    }

}
