package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.FolderItemsResponseDto;

import java.util.List;

@Getter
@Setter
public class ProgramFoldersItemsResponseDto {

    private Long id;
    private String name;
    private String description;
    private String image;
    private List<FolderItemsResponseDto> folders;

}