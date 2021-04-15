package ua.kazo.dentalacademy.service.payment;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

@Data
@RequiredArgsConstructor
public class OrderManualUpdateLogItem {

    private final String type;
    private final String user;
    private final UnifiedPaymentStatus status;

}
