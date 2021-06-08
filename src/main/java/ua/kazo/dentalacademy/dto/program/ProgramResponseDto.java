package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.ProgramCategory;

@Getter
@Setter
public class ProgramResponseDto {

    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    private ProgramCategory category;

}
