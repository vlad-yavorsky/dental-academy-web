package ua.kazo.dentalacademy.dto.order;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.purchase.PurchaseDataResponseDto;
import ua.kazo.dentalacademy.enumerated.LiqPayPaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {

    private Long id;
    private String number;
    private List<PurchaseDataResponseDto> purchaseData;
    private LiqPayPaymentStatus status;
    private LocalDateTime purchased;
    private BigDecimal price;

}
