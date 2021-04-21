package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.offering.OfferingResponseDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProgramOfferingsResponseDto {

    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    private List<OfferingResponseDto> offerings;
    private BigDecimal startingPrice;

}
