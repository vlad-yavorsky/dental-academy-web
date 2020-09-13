package ua.kazo.dentalacademy.service.payment.convertor;

import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

@Component
public class FondyPaymentStatus extends PaymentStatusConverter {

    private static final String CREATED = "created";
    private static final String PROCESSING = "processing";
    private static final String DECLINED = "declined";
    private static final String APPROVED = "approved";
    private static final String EXPIRED = "expired";
    private static final String REVERSED = "reversed";

    public FondyPaymentStatus() {
        map.put(FondyPaymentStatus.CREATED, UnifiedPaymentStatus.CREATED);
        map.put(FondyPaymentStatus.PROCESSING, UnifiedPaymentStatus.PROCESSING);
        map.put(FondyPaymentStatus.DECLINED, UnifiedPaymentStatus.FAILURE);
        map.put(FondyPaymentStatus.APPROVED, UnifiedPaymentStatus.SUCCESS);
        map.put(FondyPaymentStatus.EXPIRED, UnifiedPaymentStatus.FAILURE);
        map.put(FondyPaymentStatus.REVERSED, UnifiedPaymentStatus.REVERSED);
    }

}
