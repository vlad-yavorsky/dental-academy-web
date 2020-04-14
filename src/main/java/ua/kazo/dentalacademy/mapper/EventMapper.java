package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import ua.kazo.dentalacademy.dto.event.EventResponseDto;
import ua.kazo.dentalacademy.entity.Event;

import java.util.List;

@Mapper
public interface EventMapper {

    EventResponseDto toResponseDto(Event event);
    List<EventResponseDto> toResponseDto(List<Event> events);

}
