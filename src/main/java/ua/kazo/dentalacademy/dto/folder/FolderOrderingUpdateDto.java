package ua.kazo.dentalacademy.dto.folder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderOrderingUpdateDto {

    private Long id;
    private String name;
    private Long ordering;

}
