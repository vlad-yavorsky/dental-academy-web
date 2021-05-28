package ua.kazo.dentalacademy.dto.user;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String email;
    private LocalDateTime created;
    private boolean enabled;
    private boolean accountNonLocked;
    private String firstName;
    private String lastName;
    private String mobile;
    private LocalDate birthday;
    private Set<Role> roles;
    private String existingPhotoPath;
    private int cartItemsCount;

}
