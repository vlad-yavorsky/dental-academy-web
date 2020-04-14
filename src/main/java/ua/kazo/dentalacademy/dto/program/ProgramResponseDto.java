package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramResponseDto {

    private Long id;
    private String name;
    private String description;
    private String image;

}
