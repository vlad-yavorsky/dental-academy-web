package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import ua.kazo.dentalacademy.dto.program.ProgramCreateDto;
import ua.kazo.dentalacademy.dto.program.ProgramFoldersResponseDto;
import ua.kazo.dentalacademy.dto.program.ProgramResponseDto;
import ua.kazo.dentalacademy.dto.program.ProgramUpdateDto;
import ua.kazo.dentalacademy.entity.Program;

import java.util.List;

@Mapper(uses = {FolderMapper.class, OfferingMapper.class})
public interface ProgramMapper {

    Program toEntity(ProgramCreateDto programCreateDto);
    Program toEntity(ProgramUpdateDto programUpdateDto);

    ProgramFoldersResponseDto toFoldersResponseDto(Program program);

    ProgramResponseDto toResponseDto(Program program);
    List<ProgramResponseDto> toResponseDto(List<Program> programs);

    ProgramUpdateDto toUpdateDto(Program program);

}
