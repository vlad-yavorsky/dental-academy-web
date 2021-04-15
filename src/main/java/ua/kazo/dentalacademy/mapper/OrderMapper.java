package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.order.OrderUserPurchaseDataResponseDto;
import ua.kazo.dentalacademy.dto.order.OrderUserResponseDto;
import ua.kazo.dentalacademy.dto.order.OrderPurchaseDataResponseDto;
import ua.kazo.dentalacademy.entity.Order;

import java.util.List;

@Mapper(uses = {PurchaseDataMapper.class, UserMapper.class})
public interface OrderMapper {

    OrderPurchaseDataResponseDto toPurchaseDataResponseDto(Order order);
    List<OrderPurchaseDataResponseDto> toPurchaseDataResponseDto(Page<Order> order);

    OrderUserPurchaseDataResponseDto toUserPurchaseDataResponseDto(Order order);

    OrderUserResponseDto toUserResponseDto(Order order);
    List<OrderUserResponseDto> toUserResponseDto(Page<Order> order);

}
