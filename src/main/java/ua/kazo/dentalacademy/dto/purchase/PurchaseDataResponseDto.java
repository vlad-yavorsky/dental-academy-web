package ua.kazo.dentalacademy.dto.purchase;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.offering.OfferingFullResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PurchaseDataResponseDto {

    private OfferingFullResponseDto offering;
    private LocalDateTime purchased;
    private LocalDateTime expired;
    private BigDecimal price;

}
