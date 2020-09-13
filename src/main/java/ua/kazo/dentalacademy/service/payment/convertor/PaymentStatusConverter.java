package ua.kazo.dentalacademy.service.payment.convertor;

import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import java.util.HashMap;
import java.util.Map;

public class PaymentStatusConverter {

    protected final Map<String, UnifiedPaymentStatus> map = new HashMap<>();

    protected PaymentStatusConverter() {}

    public UnifiedPaymentStatus convertToUnifiedStatus(String status) {
        if (!map.containsKey(status)) {
            return UnifiedPaymentStatus.OTHER;
        }
        return map.get(status);
    }

}
