package ua.kazo.dentalacademy.service;

import liquibase.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.enumerated.Role;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.UserRepository;
import ua.kazo.dentalacademy.service.storage.FileSystemStorageService;
import ua.kazo.dentalacademy.service.storage.StorageProperties;
import ua.kazo.dentalacademy.util.AuthUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final FileSystemStorageService storageService;
    private final StorageProperties storageProperties;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return findByEmailFetchRoles(email);
    }

    public User findByEmailFetchRoles(String email) {
        return userRepository.findFetchRolesByEmail(email)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.USER_NOT_FOUND, email));
    }

    public User findByEmailFetchCartItems(String email) {
        return userRepository.findFetchCartItemsByEmail(email)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.USER_NOT_FOUND, email));
    }

    public User findByEmailFetchCartItemsAndOrders(String email, Supplier<?> supplier) {
        User user = userRepository.findFetchCartItemsByEmail(email)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.USER_NOT_FOUND, email));
        userRepository.findFetchOrdersByEmail(email);
        supplier.get();
        return user;
    }

    public void addItemToCart(String email, Offering offering) {
        User user = findByEmailFetchCartItems(email);
        user.getCartItems().add(offering);
    }

    public void removeItemFromCart(String email, Long offeringId) {
        User user = findByEmailFetchCartItems(email);
        user.getCartItems().removeIf(offering -> offering.getId().equals(offeringId));
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndIdNot(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }

    public void create(User user, MultipartFile photo) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRoles(Set.of(Role.USER));
        User userFromDb = userRepository.save(user);
        handlePhotoChange(photo, userFromDb);
    }

    public User update(User user, MultipartFile photo, String userEmail) {
        User userFromDb = findByEmailFetchRoles(userEmail);
        userFromDb.setEmail(user.getEmail());
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setMobile(user.getMobile());
        userFromDb.setBirthday(user.getBirthday());
        handlePhotoChange(photo, userFromDb);
        User savedUser = userRepository.save(userFromDb);
        AuthUtils.updateAuthenticationAfterCredentialsChange(savedUser);
        return savedUser;
    }

    private void handlePhotoChange(MultipartFile photo, User userFromDb) {
        if (!photo.isEmpty()) {
            String photoName = storageService.store(photo);
            if (StringUtils.isNotEmpty(userFromDb.getPhotoName())) {
                Path rootLocation = Paths.get(storageProperties.getLocation());
                Path fileToDelete = rootLocation.resolve(Paths.get(userFromDb.getPhotoName()))
                        .normalize()
                        .toAbsolutePath();
                try {
                    Files.delete(fileToDelete);
                } catch (NoSuchFileException ex) {
                    log.warn("No such file or directory: {}", fileToDelete);
                } catch (IOException ex) {
                    log.warn("File: {}, permission problems: {}", fileToDelete, ex.getMessage());
                }
            }
            userFromDb.setPhotoName(photoName);
        }
    }

    public boolean arePasswordsMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void updatePassword(User userFromDb, String newPassword) {
        userFromDb.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userFromDb);
    }

}
