package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.program.*;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.Program;
import ua.kazo.dentalacademy.util.MathUtil;

import java.math.BigDecimal;
import java.util.List;

@Mapper(uses = {FolderMapper.class, OfferingMapper.class})
public interface ProgramMapper {

    Program toEntity(ProgramCreateDto programCreateDto);
    Program toEntity(ProgramUpdateDto programUpdateDto);

    ProgramFoldersResponseDto toFoldersResponseDto(Program program);
    ProgramFoldersItemsResponseDto toFoldersItemsResponseDto(Program program);

    @Mapping(target = "startingPrice", source = "offerings", qualifiedByName = "setStartingPrice")
    ProgramOfferingsResponseDto toOfferingsResponseDto(Program program);
    List<ProgramOfferingsResponseDto> toOfferingsResponseDto(Page<Program> programs);

    ProgramResponseDto toResponseDto(Program program);
    List<ProgramResponseDto> toResponseDto(List<Program> programs);
    List<ProgramResponseDto> toResponseDto(Page<Program> programs);

    ProgramUpdateDto toUpdateDto(Program program);

    @Named("setStartingPrice")
    default BigDecimal setStartingPrice(List<Offering> offerings) {
        return offerings.stream()
                .map(offering -> MathUtil.calculateDiscountPrice(offering.getPrice(), offering.getDiscount()))
                .reduce(BigDecimal::min)
                .orElse(null);
    }

}
