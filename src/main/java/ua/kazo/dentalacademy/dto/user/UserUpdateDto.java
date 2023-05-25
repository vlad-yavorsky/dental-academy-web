package ua.kazo.dentalacademy.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateDto {

    private Long id; // todo: maybe hide ID from printing to thymeleaf html page
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String mobile;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
    private MultipartFile newPhoto;
    private String existingPhotoPath;
    private boolean removeExistingPhoto;
    private String interests;

}
