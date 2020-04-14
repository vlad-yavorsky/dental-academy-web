package ua.kazo.dentalacademy.dto.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventResponseDto {

    private Long id;
    private String name;
    private String description;
    private String image;
    private LocalDateTime date;

}
