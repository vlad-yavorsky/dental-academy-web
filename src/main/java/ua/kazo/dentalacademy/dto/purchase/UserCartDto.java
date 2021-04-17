package ua.kazo.dentalacademy.dto.purchase;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.offering.OfferingProgramsBonusesResponseDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UserCartDto {

    private List<OfferingProgramsBonusesResponseDto> cartItems;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscountPrice;
    private BigDecimal totalDiscount;

}
