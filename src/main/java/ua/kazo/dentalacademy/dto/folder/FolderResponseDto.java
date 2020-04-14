package ua.kazo.dentalacademy.dto.folder;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.FolderCategory;

@Getter
@Setter
public class FolderResponseDto {

    private Long id;
    private String name;
    private String description;
    private String image;
    private FolderCategory category;

}
