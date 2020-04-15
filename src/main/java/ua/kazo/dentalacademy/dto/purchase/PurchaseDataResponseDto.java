package ua.kazo.dentalacademy.dto.purchase;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.offering.PurchaseDataOfferingResponseDto;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PurchaseDataResponseDto {

    private String name;
    private OfferingType type;
    private BigDecimal price;
    private LocalDateTime purchased;
    private LocalDateTime expired;
    private PurchaseDataOfferingResponseDto offering;

}
