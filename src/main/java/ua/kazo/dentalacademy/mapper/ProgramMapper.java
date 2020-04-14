package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ua.kazo.dentalacademy.dto.program.*;
import ua.kazo.dentalacademy.entity.Program;

import java.util.List;

@Mapper(uses = {FolderMapper.class, OfferingMapper.class})
public interface ProgramMapper {

    Program toEntity(ProgramCreateDto programCreateDto);
    Program toEntity(ProgramUpdateDto programUpdateDto);

    ProgramFoldersResponseDto toFoldersResponseDto(Program program);

    ProgramResponseDto toResponseDto(Program program);
    List<ProgramResponseDto> toResponseDto(List<Program> programs);

//    @Mapping(source = "offerings", target = "bought", qualifiedByName = "isBought")
    ProgramShopResponseDto toShopResponseDto(Program offerings, @Context String userEmail);
    List<ProgramShopResponseDto> toShopResponseDto(List<Program> program, @Context String userEmail);

    ProgramUpdateDto toUpdateDto(Program program);

//    default boolean isBought(List<Offering> offerings, @Context String userEmail) {
//        return offerings.stream()
//                .anyMatch(offering -> offering.getPurchaseData().stream()
//                        .anyMatch(purchaseData -> purchaseData.getUser().getEmail().equals(userEmail)));
//    }

}
