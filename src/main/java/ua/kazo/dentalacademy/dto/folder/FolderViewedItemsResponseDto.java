package ua.kazo.dentalacademy.dto.folder;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.item.FolderViewedItemResponseDto;
import ua.kazo.dentalacademy.enumerated.FolderCategory;

import java.util.List;

@Getter
@Setter
public class FolderViewedItemsResponseDto {

    private Long id;
    private String name;
    private String description;
    private String image;
    private FolderCategory category;
    private List<FolderViewedItemResponseDto> items;
    private int folderCompletePercentage;

}