package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ua.kazo.dentalacademy.entity.Folder;
import ua.kazo.dentalacademy.entity.Program;

import java.util.List;

@Mapper
public interface IdsMapper {

    @Named("longListToProgramList")
    default List<Program> longListToProgramList(List<Long> programIds) {
        return programIds.stream()
                .map(id -> {
                    Program program = new Program();
                    program.setId(id);
                    return program;
                })
                .toList();
    }

    @Named("programListToLongList")
    default List<Long> programListToLongList(List<Program> programs) {
        return programs.stream()
                .map(Program::getId)
                .toList();
    }

    @Named("longListToFolderList")
    default List<Folder> longListToFolderList(List<Long> folderIds) {
        return folderIds.stream()
                .map(id -> {
                    Folder folder = new Folder();
                    folder.setId(id);
                    return folder;
                })
                .toList();
    }

    @Named("folderListToLongList")
    default List<Long> folderListToLongList(List<Folder> folders) {
        return folders.stream()
                .map(Folder::getId)
                .toList();
    }

}
