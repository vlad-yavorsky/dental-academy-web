package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProgramUpdateDto {

    private Long id;
    @NotBlank
    private String name;
    private String description;
    private String image;

}
