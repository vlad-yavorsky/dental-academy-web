package ua.kazo.dentalacademy.dto.event;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.dto.user.UserRegisteredForEventInfoDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EventRegisteredUsersDto {

    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    private LocalDateTime date;
    private List<UserRegisteredForEventInfoDto> registeredUsers;

}
