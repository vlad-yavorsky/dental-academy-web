package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.FolderResponseDto;

import java.util.List;

@Getter
@Setter
public class ProgramFoldersResponseDto {

    private Long id;
    private String name;
    private String description;
    private String image;
    private List<FolderResponseDto> folders;

}
