package ua.kazo.dentalacademy.dto.offering;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.program.ProgramResponseDto;

import java.util.List;

@Getter
@Setter
public class PurchaseDataOfferingResponseDto {

    private List<ProgramResponseDto> programs;
    private List<ProgramResponseDto> bonuses;

}
