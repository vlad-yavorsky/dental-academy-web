package ua.kazo.dentalacademy.dto.folder;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemCreateDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FolderCreateDto {

    @NotBlank
    private String name;
    @NotEmpty
    private List<FolderItemCreateDto> items = new ArrayList<>();
    private Long programId;
    private int ordering;

}
