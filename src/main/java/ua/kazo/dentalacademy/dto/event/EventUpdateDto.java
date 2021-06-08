package ua.kazo.dentalacademy.dto.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventUpdateDto {

    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime date;

}
