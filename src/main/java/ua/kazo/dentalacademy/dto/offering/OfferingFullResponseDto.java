package ua.kazo.dentalacademy.dto.offering;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.FolderResponseDto;
import ua.kazo.dentalacademy.dto.program.ProgramResponseDto;
import ua.kazo.dentalacademy.enumerated.OfferingType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OfferingFullResponseDto {

    private Long id;
    private String name;
    private String description;
    private OfferingType type;
    private BigDecimal price;
    private byte term;
    private LocalDateTime activated;
    private LocalDateTime deactivated;
    private List<ProgramResponseDto> programs;
    private List<FolderResponseDto> folders;

}
