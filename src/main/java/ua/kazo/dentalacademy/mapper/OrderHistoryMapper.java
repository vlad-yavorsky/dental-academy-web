package ua.kazo.dentalacademy.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.kazo.dentalacademy.dto.order.OrderHistoryResponseDto;
import ua.kazo.dentalacademy.entity.OrderHistory;

import java.util.List;

@Mapper
public interface OrderHistoryMapper {

    @Mapping(target = "data", source = "data", qualifiedByName = "setData")
    OrderHistoryResponseDto toResponseDto(OrderHistory orderHistory, @Context ObjectMapper objectMapper);
    List<OrderHistoryResponseDto> toResponseDto(List<OrderHistory> orderHistory, @Context ObjectMapper objectMapper);

    @Named("setData")
    default String setData(String data, @Context ObjectMapper objectMapper) throws JsonProcessingException {
        JsonNode jsonNodeTree = objectMapper.readTree(data);
        return new YAMLMapper().writeValueAsString(jsonNodeTree)
                .substring(4)
                .replace("\n", "<br/>");
    }

}
