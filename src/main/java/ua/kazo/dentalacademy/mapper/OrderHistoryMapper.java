package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import ua.kazo.dentalacademy.dto.order.OrderHistoryResponseDto;
import ua.kazo.dentalacademy.entity.OrderHistory;

import java.util.List;

@Mapper
public interface OrderHistoryMapper {

    OrderHistoryResponseDto toResponseDto(OrderHistory orderHistory);
    List<OrderHistoryResponseDto> toResponseDto(List<OrderHistory> orderHistory);

}
