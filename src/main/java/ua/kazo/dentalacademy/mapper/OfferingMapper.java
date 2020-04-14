package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kazo.dentalacademy.dto.offering.OfferingCreateDto;
import ua.kazo.dentalacademy.dto.offering.OfferingFullResponseDto;
import ua.kazo.dentalacademy.dto.offering.OfferingResponseDto;
import ua.kazo.dentalacademy.dto.offering.OfferingUpdateDto;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.util.MathUtil;

import java.math.BigDecimal;
import java.util.List;

@Mapper(uses = {IdsMapper.class})
public interface OfferingMapper {

    @Mapping(target = "programs", source = "programs", qualifiedByName = "longListToProgramList")
    @Mapping(target = "folders", source = "folders", qualifiedByName = "longListToFolderList")
    Offering toEntity(OfferingCreateDto offeringCreateDto);
    @Mapping(target = "programs", source = "programs", qualifiedByName = "longListToProgramList")
    @Mapping(target = "folders", source = "folders", qualifiedByName = "longListToFolderList")
    Offering toEntity(OfferingUpdateDto offeringUpdateDto);

    @Mapping(target = "programs", source = "programs", qualifiedByName = "programListToLongList")
    @Mapping(target = "folders", source = "folders", qualifiedByName = "folderListToLongList")
    OfferingUpdateDto toUpdateDto(Offering offering);

    @Mapping(target = "discountPrice", source = "offering", qualifiedByName = "calculateDiscountPrice")
    OfferingResponseDto toResponseDto(Offering offering);
    List<OfferingResponseDto> toResponseDto(List<Offering> offerings);

    @Mapping(target = "discountPrice", source = "offering", qualifiedByName = "calculateDiscountPrice")
    OfferingFullResponseDto toFullResponseDto(Offering offering);
    List<OfferingFullResponseDto> toFullResponseDto(List<Offering> offerings);

    default BigDecimal calculateDiscountPrice(Offering offering) {
        return MathUtil.calculateDiscountPrice(offering.getPrice(), offering.getDiscount());
    }

}
