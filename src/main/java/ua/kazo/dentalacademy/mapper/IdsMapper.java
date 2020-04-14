package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.Program;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface IdsMapper {

    default List<Program> longListToProgramList(List<Long> programIds) {
        return programIds.stream()
                .map(id -> {
                    Program program = new Program();
                    program.setId(id);
                    return program;
                })
                .collect(Collectors.toList());
    }

    default List<Long> programListToLongList(List<Program> programs) {
        return programs.stream()
                .map(Program::getId)
                .collect(Collectors.toList());
    }

    default List<Folder> longListToFolderList(List<Long> folderIds) {
        return folderIds.stream()
                .map(id -> {
                    Folder folder = new Folder();
                    folder.setId(id);
                    return folder;
                })
                .collect(Collectors.toList());
    }

    default List<Long> folderListToLongList(List<Folder> folders) {
        return folders.stream()
                .map(Folder::getId)
                .collect(Collectors.toList());
    }

}
