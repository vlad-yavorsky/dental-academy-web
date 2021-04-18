package ua.kazo.dentalacademy.dto.order;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderHistoryResponseDto {

    private LocalDateTime created;
    private UnifiedPaymentStatus status;
    private String data;

}
