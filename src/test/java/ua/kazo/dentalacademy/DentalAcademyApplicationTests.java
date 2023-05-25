package ua.kazo.dentalacademy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ua.kazo.dentalacademy.dto.user.UserCreateDto;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.mapper.UserMapper;
import ua.kazo.dentalacademy.repository.UserRepository;
import ua.kazo.dentalacademy.service.EmailService;
import ua.kazo.dentalacademy.service.UserService;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Locale;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
class DentalAcademyApplicationTests {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private final Locale localeEN = Locale.US;
    private final Locale localeUK = new Locale("uk", "UA");
    private final Locale localeRU = new Locale("ru", "RU");

    private User createTestUser() {
        UserCreateDto userDto = new UserCreateDto();
        userDto.setFirstName("Test");
        userDto.setLastName("Test");
        userDto.setMobile("380001112233");
        userDto.setEmail("test@example.com");
        userDto.setPassword("test");
        userDto.setConfirmPassword("test");
        return userMapper.toEntity(userDto);
    }

    private MultipartFile createMockFile() {
        return new MockMultipartFile("test.png", new byte[0]);
    }

    @Test
    @Disabled
    void registerUser() throws MessagingException {
        userRepository.findFetchRolesByEmail("test@example.com")
                .ifPresent(user -> userRepository.delete(user));
        userService.register(createTestUser(), createMockFile(), localeUK);
    }

    @Test
    @Disabled
    void sendActivationToken() throws MessagingException {
        User user = userRepository.findFetchRolesByEmail("test@example.com").
                orElseThrow(() -> new RuntimeException("No such user"));
        if (ObjectUtils.isEmpty(user.getActivationToken()) || LocalDateTime.now().isAfter(user.getTokenExpiryDate())) {
            throw new RuntimeException("Activation token expired");
        }
        emailService.sendUserActivationLink(user, localeUK);
    }

}
