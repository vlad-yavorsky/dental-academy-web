package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemCreateDto;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemResponseDto;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemUpdateDto;
import ua.kazo.dentalacademy.dto.folder.item.FolderViewedItemResponseDto;
import ua.kazo.dentalacademy.entity.FolderItem;
import ua.kazo.dentalacademy.entity.ViewedFolderItem;

import java.util.List;

@Mapper
public interface FolderItemMapper {

    FolderItem toEntity(FolderItemCreateDto folderItemCreateDto);
    @Mapping(source = "folderId", target = "folder.id")
    FolderItem toEntity(FolderItemUpdateDto folderItemUpdateDto);

    @Mapping(source = "folder.id", target = "folderId")
    FolderItemResponseDto toResponseDto(FolderItem folderItem);
    List<FolderItemResponseDto> toResponseDto(List<FolderItem> folderItems);

    @Mapping(source = "folder.id", target = "folderId")
    FolderItemUpdateDto toUpdateDto(FolderItem folderItem);

    @Mapping(target = "viewedByUser", source = "viewedFolderItems", qualifiedByName = "isViewedByUser")
    FolderViewedItemResponseDto toFolderViewedItemResponseDto(FolderItem folderItem, @Context Long userId);

    static boolean isViewedByUser(List<ViewedFolderItem> viewedFolderItems, @Context Long userId) {
        return viewedFolderItems.stream()
                .anyMatch(viewedFolderItem -> viewedFolderItem.getId().getUserId().equals(userId));
    }

}
