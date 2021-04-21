package ua.kazo.dentalacademy.dto.folder;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemUpdateDto;
import ua.kazo.dentalacademy.enumerated.FolderCategory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FolderUpdateDto {

    private Long id;
    @NotBlank
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    @NotNull
    private FolderCategory category;
    @NotEmpty
    private List<FolderItemUpdateDto> items = new ArrayList<>();
    private List<Long> programs = new ArrayList<>();

}
