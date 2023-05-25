package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.ProgramCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ProgramUpdateDto {

    private Long id;
    @NotBlank
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    @NotNull
    private ProgramCategory category;

}
