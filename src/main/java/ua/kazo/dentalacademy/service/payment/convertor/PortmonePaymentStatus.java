package ua.kazo.dentalacademy.service.payment.convertor;

import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

@Component
public class PortmonePaymentStatus extends PaymentStatusConverter {

    private static final String PAYED = "PAYED";
    private static final String PREAUTH = "PREAUTH";
    private static final String REJECTED = "REJECTED";
    private static final String CREATED = "CREATED";

    public PortmonePaymentStatus() {
        map.put(PortmonePaymentStatus.PAYED, UnifiedPaymentStatus.SUCCESS);
        map.put(PortmonePaymentStatus.REJECTED, UnifiedPaymentStatus.FAILURE);
        map.put(PortmonePaymentStatus.CREATED, UnifiedPaymentStatus.CREATED);
    }

}
