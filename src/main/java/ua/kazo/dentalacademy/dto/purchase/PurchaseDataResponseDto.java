package ua.kazo.dentalacademy.dto.purchase;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.FolderResponseDto;
import ua.kazo.dentalacademy.dto.offering.PurchaseDataOfferingResponseDto;
import ua.kazo.dentalacademy.dto.program.ProgramResponseDto;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PurchaseDataResponseDto {

    private String name;
    private OfferingType type;
    private BigDecimal price;
    private LocalDateTime expired;
    private PurchaseDataOfferingResponseDto offering;
    private List<ProgramResponseDto> programs;
    private List<FolderResponseDto> folders;

}
