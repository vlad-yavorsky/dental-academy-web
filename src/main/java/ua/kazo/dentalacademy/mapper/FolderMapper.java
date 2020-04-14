package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kazo.dentalacademy.dto.folder.FolderCreateDto;
import ua.kazo.dentalacademy.dto.folder.FolderItemsResponseDto;
import ua.kazo.dentalacademy.dto.folder.FolderResponseDto;
import ua.kazo.dentalacademy.dto.folder.FolderUpdateDto;
import ua.kazo.dentalacademy.entity.Folder;

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

    FolderItemsResponseDto toItemsResponseDto(Folder folder);
    List<FolderItemsResponseDto> toItemsResponseDto(List<Folder> folders);

}
