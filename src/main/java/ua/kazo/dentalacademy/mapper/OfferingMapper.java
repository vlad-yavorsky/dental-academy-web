package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kazo.dentalacademy.dto.offering.*;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.PurchaseData;
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

    PurchaseDataOfferingResponseDto toPurchaseDataResponseDto(Offering offering);
    List<PurchaseDataOfferingResponseDto> toPurchaseDataResponseDto(List<Offering> offerings);

    @Mapping(target = "discountPrice", source = "offering", qualifiedByName = "calculateDiscountPrice")
    @Mapping(target = "purchased", source = "offering", qualifiedByName = "isOfferingPurchasedByUser")
    ShopItemOfferingResponseDto toShopItemResponseDto(Offering offering, @Context List<PurchaseData> purchasesByUser);
    List<ShopItemOfferingResponseDto> toShopItemResponseDto(List<Offering> offerings, @Context List<PurchaseData> purchasesByUser);

    default BigDecimal calculateDiscountPrice(Offering offering) {
        return MathUtil.calculateDiscountPrice(offering.getPrice(), offering.getDiscount());
    }

    default boolean isOfferingPurchasedByUser(Offering offering, @Context List<PurchaseData> purchasesByUser) {
        return purchasesByUser.stream()
                .anyMatch(purchase -> offering.getId().equals(purchase.getOffering().getId()));
    }

}
