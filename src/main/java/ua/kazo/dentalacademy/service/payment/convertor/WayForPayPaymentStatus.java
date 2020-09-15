package ua.kazo.dentalacademy.service.payment.convertor;

import org.springframework.stereotype.Component;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

@Component
public class WayForPayPaymentStatus extends PaymentStatusConverter {

    private static final String IN_PROCESSING = "InProcessing";
    private static final String WAITING_AUTH_COMPLETE = "WaitingAuthComplete";
    private static final String APPROVED = "Approved";
    private static final String PENDING = "Pending";
    private static final String EXPIRED = "Expired";
    private static final String REFUNDED = "Refunded";
    private static final String VOIDED = "Voided";
    private static final String DECLINED = "Declined";
    private static final String REFUND_IN_PROCESSING = "RefundInProcessing";

    public WayForPayPaymentStatus() {
        map.put(WayForPayPaymentStatus.IN_PROCESSING, UnifiedPaymentStatus.PROCESSING);
        map.put(WayForPayPaymentStatus.WAITING_AUTH_COMPLETE, UnifiedPaymentStatus.PROCESSING);
        map.put(WayForPayPaymentStatus.APPROVED, UnifiedPaymentStatus.SUCCESS);
        map.put(WayForPayPaymentStatus.PENDING, UnifiedPaymentStatus.PROCESSING);
        map.put(WayForPayPaymentStatus.EXPIRED, UnifiedPaymentStatus.FAILURE);
        map.put(WayForPayPaymentStatus.REFUNDED, UnifiedPaymentStatus.REVERSED);
        map.put(WayForPayPaymentStatus.VOIDED, UnifiedPaymentStatus.REVERSED);
        map.put(WayForPayPaymentStatus.DECLINED, UnifiedPaymentStatus.FAILURE);
        map.put(WayForPayPaymentStatus.REFUND_IN_PROCESSING, UnifiedPaymentStatus.PROCESSING);
    }

}
