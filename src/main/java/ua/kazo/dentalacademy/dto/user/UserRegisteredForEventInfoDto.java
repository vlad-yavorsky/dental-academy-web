package ua.kazo.dentalacademy.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserRegisteredForEventInfoDto {

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private LocalDate birthday;

}
