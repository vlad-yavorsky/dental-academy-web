package ua.kazo.dentalacademy.dto.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventPrincipalRegistrationInfoDto {

    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String image;
    private LocalDateTime date;
    private boolean userRegistered;
    private boolean futureDate;

}
