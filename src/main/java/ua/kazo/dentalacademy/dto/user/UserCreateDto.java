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
public class UserCreateDto {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String mobile;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
    private MultipartFile newPhoto;
    private String interests;

}
