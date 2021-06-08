package ua.kazo.dentalacademy.service.payment.convertor;

import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;

import java.util.EnumMap;
import java.util.Map;

@Component
public class PaymentStatusConverterHolder {

    private static final Map<PaymentProvider, PaymentStatusConverter> map = new EnumMap<>(PaymentProvider.class);

    public PaymentStatusConverterHolder(LiqPayPaymentStatus liqPay, FondyPaymentStatus fondy,
                                        PortmonePaymentStatus portmone, WayForPayPaymentStatus wayForPay) {
        map.put(PaymentProvider.LIQPAY, liqPay);
        map.put(PaymentProvider.FONDY, fondy);
        map.put(PaymentProvider.PORTMONE, portmone);
        map.put(PaymentProvider.WAYFORPAY, wayForPay);
    }

    public static PaymentStatusConverter get(PaymentProvider provider) {
        return map.get(provider);
    }

}
