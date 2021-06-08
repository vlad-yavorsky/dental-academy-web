package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.offering.*;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.enumerated.UnifiedPaymentStatus;
import ua.kazo.dentalacademy.util.MathUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(uses = IdsMapper.class)
public interface OfferingMapper {

    @Mapping(target = "programs", source = "programs", qualifiedByName = "longListToProgramList")
    @Mapping(target = "bonuses", source = "bonuses", qualifiedByName = "longListToProgramList")
    Offering toEntity(OfferingCreateDto offeringCreateDto);
    @Mapping(target = "programs", source = "programs", qualifiedByName = "longListToProgramList")
    @Mapping(target = "bonuses", source = "bonuses", qualifiedByName = "longListToProgramList")
    Offering toEntity(OfferingUpdateDto offeringUpdateDto);

    @Mapping(target = "programs", source = "programs", qualifiedByName = "programListToLongList")
    @Mapping(target = "bonuses", source = "bonuses", qualifiedByName = "programListToLongList")
    OfferingUpdateDto toUpdateDto(Offering offering);

    @Mapping(target = "discountPrice", source = "offering", qualifiedByName = "calculateDiscountPrice")
    OfferingResponseDto toResponseDto(Offering offering);
    List<OfferingResponseDto> toResponseDto(List<Offering> offerings);
    List<OfferingResponseDto> toResponseDto(Page<Offering> offerings);

    @Mapping(target = "discountPrice", source = "offering", qualifiedByName = "calculateDiscountPrice")
    OfferingProgramsBonusesResponseDto toProgramsBonusesResponseDto(Offering offering);
    List<OfferingProgramsBonusesResponseDto> toProgramsBonusesResponseDto(List<Offering> offerings);
    List<OfferingProgramsBonusesResponseDto> toProgramsBonusesResponseDto(Page<Offering> offerings);

    PurchaseDataOfferingResponseDto toPurchaseDataResponseDto(Offering offering);
    List<PurchaseDataOfferingResponseDto> toPurchaseDataResponseDto(List<Offering> offerings);

    @Mapping(target = "discountPrice", source = "offering", qualifiedByName = "calculateDiscountPrice")
    @Mapping(target = "purchased", source = "offering", qualifiedByName = "isOfferingPurchasedByUser")
    @Mapping(target = "inCart", source = "offering", qualifiedByName = "isOfferingInUserCart")
    ShopItemOfferingResponseDto toShopItemResponseDto(Offering offering, @Context User user);
    List<ShopItemOfferingResponseDto> toShopItemResponseDto(List<Offering> offerings, @Context User user);

    @Named("calculateDiscountPrice")
    default BigDecimal calculateDiscountPrice(Offering offering) {
        return MathUtil.calculateDiscountPrice(offering.getPrice(), offering.getDiscount());
    }

    @Named("isOfferingPurchasedByUser")
    default boolean isOfferingPurchasedByUser(Offering offering, @Context User user) {
        LocalDateTime now = LocalDateTime.now();
        return user.getOrders().stream().anyMatch(order -> UnifiedPaymentStatus.SUCCESS == order.getStatus()
                && order.getPurchaseData().stream().anyMatch(purchaseData -> offering.getId().equals(purchaseData.getOffering().getId())
                && now.isBefore(purchaseData.getExpired())));
    }

    @Named("isOfferingInUserCart")
    default boolean isOfferingInUserCart(Offering offering, @Context User user) {
        return user.getCartItems().stream()
                .anyMatch(cartItem -> offering.getId().equals(cartItem.getId()));
    }

}
