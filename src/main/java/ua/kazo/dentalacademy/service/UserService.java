package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.util.AuthUtils;
import ua.kazo.dentalacademy.enumerated.Role;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return findByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.USER_NOT_FOUND, email));
    }

    public List<User> findAll() {
        return userRepository.findAll(Sort.by("id"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndIdNot(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }

    public void create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user);
    }

    public User update(User user, String userEmail) {
        User userFromDb = findByEmail(userEmail);
        userFromDb.setEmail(user.getEmail());
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setMobile(user.getMobile());
        userFromDb.setBirthday(user.getBirthday());
        User savedUser = userRepository.save(userFromDb);
        AuthUtils.updateAuthenticationAfterCredentialsChange(savedUser);
        return savedUser;
    }

    public boolean arePasswordsMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void updatePassword(User userFromDb, String newPassword) {
        userFromDb.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userFromDb);
    }

}
