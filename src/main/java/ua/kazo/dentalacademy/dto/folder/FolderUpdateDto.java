package ua.kazo.dentalacademy.dto.folder;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemUpdateDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FolderUpdateDto {

    private Long id;
    @NotBlank
    private String name;
    @NotEmpty
    private List<FolderItemUpdateDto> items = new ArrayList<>();
    private Long programId;
    private int ordering;

}
