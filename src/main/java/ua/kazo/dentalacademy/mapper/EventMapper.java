package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.event.EventCreateDto;
import ua.kazo.dentalacademy.dto.event.EventResponseDto;
import ua.kazo.dentalacademy.dto.event.EventUpdateDto;
import ua.kazo.dentalacademy.dto.event.EventUserRegisteredResponseDto;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.entity.EventUser;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EventMapper {

    EventUpdateDto toUpdateDto(Event event);

    EventResponseDto toResponseDto(Event event);
    List<EventResponseDto> toResponseDto(Page<Event> events);

    Event toEntity(EventCreateDto dto);
    Event toEntity(EventUpdateDto dto);

    @Mapping(source = "registeredUsers", target = "userRegistered", qualifiedByName = "setUserRegistered")
    @Mapping(target = "futureDate", source = "date", qualifiedByName = "setFutureDate")
    EventUserRegisteredResponseDto toUserRegisteredResponseDto(Event event, @Context Long userId);
    List<EventUserRegisteredResponseDto> toUserRegisteredResponseDto(Page<Event> events, @Context Long userId);

    @Named("setUserRegistered")
    default boolean setUserRegistered(List<EventUser> registeredUsers, @Context Long userId) {
        return registeredUsers.stream().anyMatch(eventUser -> eventUser.getId().getUserId().equals(userId));
    }

    @Named("setFutureDate")
    default boolean setFutureDate(LocalDateTime date, @Context Long userId) {
        return date != null && date.isAfter(LocalDateTime.now());
    }

}
