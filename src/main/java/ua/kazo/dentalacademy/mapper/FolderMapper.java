package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.folder.*;
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

    FolderItemsResponseDto toItemsResponseDto(Folder folder);
    List<FolderItemsResponseDto> toItemsResponseDto(List<Folder> folder);

    @Mapping(target = "completionPercentage", source = "items", qualifiedByName = "setCompletionPercentage")
    FolderViewedItemsResponseDto toViewedItemsResponseDto(Folder folder, @Context Long userId);

    @Named("setCompletionPercentage")
    default int setCompletionPercentage(List<FolderItem> items, @Context Long userId) {
        Integer viewedItemsCount = items.stream()
                .map(folderItem -> FolderItemMapper.isViewedByUser(folderItem.getViewedFolderItems(), userId) ? 1 : 0)
                .reduce(Integer::sum)
                .orElse(0);
        return viewedItemsCount * 100 / items.size();
    }

}
