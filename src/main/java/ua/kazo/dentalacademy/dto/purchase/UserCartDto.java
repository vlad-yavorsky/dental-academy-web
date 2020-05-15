package ua.kazo.dentalacademy.dto.purchase;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.offering.OfferingResponseDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UserCartDto {

    private List<OfferingResponseDto> cartItems;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscountPrice;

}
