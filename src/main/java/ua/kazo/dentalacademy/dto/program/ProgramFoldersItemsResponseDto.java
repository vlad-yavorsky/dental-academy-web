package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.FolderItemsResponseDto;
import ua.kazo.dentalacademy.enumerated.ProgramCategory;

import java.util.List;

@Getter
@Setter
public class ProgramFoldersItemsResponseDto {

    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    private List<FolderItemsResponseDto> folders;
    private ProgramCategory category;

}
