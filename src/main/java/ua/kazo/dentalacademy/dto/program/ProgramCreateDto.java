package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProgramCreateDto {

    @NotBlank
    private String name;
    private String description;
    private String image;

}
