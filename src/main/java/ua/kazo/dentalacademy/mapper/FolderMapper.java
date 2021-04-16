package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.folder.FolderCreateDto;
import ua.kazo.dentalacademy.dto.folder.FolderResponseDto;
import ua.kazo.dentalacademy.dto.folder.FolderUpdateDto;
import ua.kazo.dentalacademy.dto.folder.FolderViewedItemsResponseDto;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.FolderItem;

import java.util.List;

@Mapper(uses = {FolderItemMapper.class, IdsMapper.class})
public interface FolderMapper {

    @Mapping(target = "programs", source = "programs", qualifiedByName = "longListToProgramList")
    Folder toEntity(FolderCreateDto folderCreateDto);
    @Mapping(target = "programs", source = "programs", qualifiedByName = "longListToProgramList")
    Folder toEntity(FolderUpdateDto folderUpdateDto);

    @Mapping(target = "programs", source = "programs", qualifiedByName = "programListToLongList")
    FolderUpdateDto toUpdateDto(Folder folder);

    FolderResponseDto toResponseDto(Folder folder);
    List<FolderResponseDto> toResponseDto(List<Folder> folders);
    List<FolderResponseDto> toResponseDto(Page<Folder> folders);

    @Mapping(target = "folderCompletePercentage", source = "items", qualifiedByName = "setFolderCompletePercentage")
    FolderViewedItemsResponseDto toViewedItemsResponseDto(Folder folder, @Context Long userId);

    default int setFolderCompletePercentage(List<FolderItem> items, @Context Long userId) {
        Integer viewedItemsCount = items.stream()
                .map(folderItem -> FolderItemMapper.isViewedByUser(folderItem.getViewedFolderItems(), userId) ? 1 : 0)
                .reduce(Integer::sum)
                .orElse(0);
        return viewedItemsCount * 100 / items.size();
    }

}
