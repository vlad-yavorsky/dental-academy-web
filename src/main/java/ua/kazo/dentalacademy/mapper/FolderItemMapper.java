package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemCreateDto;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemResponseDto;
import ua.kazo.dentalacademy.dto.folder.item.FolderItemUpdateDto;
import ua.kazo.dentalacademy.entity.FolderItem;

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

}
