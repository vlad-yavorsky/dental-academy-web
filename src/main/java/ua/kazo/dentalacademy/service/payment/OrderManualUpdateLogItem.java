package ua.kazo.dentalacademy.service.payment;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@RequiredArgsConstructor
public class OrderManualUpdateLogItem {

    private final String type;
    private final String user;
    private final UnifiedPaymentStatus status;
    private final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

}
