package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import ua.kazo.dentalacademy.dto.purchase.PurchaseDataResponseDto;
import ua.kazo.dentalacademy.entity.PurchaseData;

import java.util.List;

@Mapper(uses = OfferingMapper.class)
public interface PurchaseDataMapper {

    PurchaseDataResponseDto toResponseDto(PurchaseData purchaseData);
    List<PurchaseDataResponseDto> toResponseDto(List<PurchaseData> purchaseData);

}
