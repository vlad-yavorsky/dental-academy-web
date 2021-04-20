package ua.kazo.dentalacademy.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserRegisteredForEventInfoDto {

    private Long id;
    private String email;
    private LocalDateTime created;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String mobile;
    private LocalDate birthday;
    private LocalDate registerDate;

}
