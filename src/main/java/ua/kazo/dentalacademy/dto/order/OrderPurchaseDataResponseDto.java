package ua.kazo.dentalacademy.dto.order;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.purchase.PurchaseDataResponseDto;
import ua.kazo.dentalacademy.enumerated.PaymentProvider;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderPurchaseDataResponseDto {

    private Long id;
    private String number;
    private UnifiedPaymentStatus status;
    private PaymentProvider provider;
    private LocalDateTime created;
    private LocalDateTime purchased;
    private BigDecimal price;
    private List<PurchaseDataResponseDto> purchaseData;

}
