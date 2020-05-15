package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.order.OrderResponseDto;
import ua.kazo.dentalacademy.entity.Order;

import java.util.List;

@Mapper(uses = PurchaseDataMapper.class)
public interface OrderMapper {

    OrderResponseDto toResponseDto(Order order);
    List<OrderResponseDto> toResponseDto(Page<Order> order);

}
