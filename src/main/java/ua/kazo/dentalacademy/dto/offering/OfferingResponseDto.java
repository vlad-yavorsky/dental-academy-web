package ua.kazo.dentalacademy.dto.offering;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OfferingResponseDto {

    private Long id;
    private String name;
    private String description;
    private OfferingType type;
    private BigDecimal price;
    private Byte discount;
    private BigDecimal discountPrice;
    private byte term;
    private LocalDateTime activated;
    private LocalDateTime deactivated;

}
