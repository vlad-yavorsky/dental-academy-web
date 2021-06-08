package ua.kazo.dentalacademy.dto.folder;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemResponseDto;

import java.util.List;

@Getter
@Setter
public class FolderItemsResponseDto {

    private Long id;
    private String name;
    private List<FolderItemResponseDto> items;

}
