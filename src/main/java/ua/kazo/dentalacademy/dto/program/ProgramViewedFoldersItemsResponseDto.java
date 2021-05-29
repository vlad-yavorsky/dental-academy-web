package ua.kazo.dentalacademy.dto.program;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.FolderViewedItemsResponseDto;

import java.util.List;

@Getter
@Setter
public class ProgramViewedFoldersItemsResponseDto {

    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    private List<FolderViewedItemsResponseDto> folders;
    private int completionPercentage;

}
