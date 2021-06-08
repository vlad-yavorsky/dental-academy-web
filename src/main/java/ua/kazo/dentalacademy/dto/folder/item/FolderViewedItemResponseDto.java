package ua.kazo.dentalacademy.dto.folder.item;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.FolderItemType;

@Getter
@Setter
public class FolderViewedItemResponseDto {

    private Long id;
    private String name;
    private String link;
    private FolderItemType type;
    private Long folderId;
    private int ordering;
    private boolean viewedByUser;

}
