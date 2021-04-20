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
import ua.kazo.dentalacademy.service.storage.StorageService;
import ua.kazo.dentalacademy.util.AuthUtils;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final StorageService storageService;
    private final OfferingService offeringService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = findByEmailFetchRoles(email);
        user.setCartItemsCount(countCartItems(user.getEmail()));
        return user;
    }

    public User findByEmailFetchRoles(String email) {
        return userRepository.findFetchRolesByEmail(email)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.USER_NOT_FOUND, email));
    }

    public User findByEmailFetchCartItems(String email) {
        User user = userRepository.findFetchCartItemsByEmail(email)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.USER_NOT_FOUND, email));
        user.setCartItemsCount(user.getCartItems().size());
        return user;
    }

    public User findByEmailFetchCartItemsAndProgramsAndBonuses(String email) {
        User user = userRepository.findFetchCartItemsByEmail(email)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.USER_NOT_FOUND, email));
        Set<Long> offeringIds = user.getCartItems().stream().map(Offering::getId).collect(Collectors.toSet());
        offeringService.findAllByIdInFetchProgramsAndFolders(offeringIds);
        user.setCartItemsCount(user.getCartItems().size());
        return user;
    }

    public User findByEmailFetchCartItemsAndOrders(String email, Supplier<?> supplier) {
        User user = userRepository.findFetchCartItemsByEmail(email)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.USER_NOT_FOUND, email));
        userRepository.findFetchOrdersByEmail(email);
        supplier.get();
        return user;
    }

    public User addItemToCart(String email, Long offeringId) {
        Offering offering = offeringService.findByIdAndActive(offeringId);
        User user = findByEmailFetchCartItems(email);
        user.getCartItems().add(offering);
        user.incCartItemsCount();
        return user;
    }

    public User removeItemFromCart(String email, Long offeringId) {
        User user = findByEmailFetchCartItems(email);
        if (user.getCartItems().removeIf(offering -> offering.getId().equals(offeringId))) {
            user.decCartItemsCount();
        }
        return user;
    }

    public int countCartItems(String email) {
        return userRepository.countCartItems(email);
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
        handlePhotoChange(photo, false, userFromDb);
    }

    public User update(User user, MultipartFile photo, boolean isRemoveExistingPhoto, String userEmail) {
        User userFromDb = findByEmailFetchRoles(userEmail);
        userFromDb.setEmail(user.getEmail());
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setMobile(user.getMobile());
        userFromDb.setBirthday(user.getBirthday());
        userFromDb.setInterests(user.getInterests());
        handlePhotoChange(photo, isRemoveExistingPhoto, userFromDb);
        User savedUser = userRepository.save(userFromDb);
        AuthUtils.updateAuthenticationAfterCredentialsChange(savedUser);
        userFromDb.setCartItemsCount(userFromDb.getCartItems().size());
        return savedUser;
    }

    private void handlePhotoChange(MultipartFile newPhoto, boolean isRemoveExistingPhoto, User userFromDb) {
        boolean uploadNewPhoto = !newPhoto.isEmpty();
        if ((uploadNewPhoto || isRemoveExistingPhoto) && StringUtils.isNotEmpty(userFromDb.getPhotoName())) {
            storageService.delete(userFromDb.getPhotoName());
            userFromDb.setPhotoName(null);
        }
        if (uploadNewPhoto) {
            String newPhotoName = storageService.store(newPhoto);
            userFromDb.setPhotoName(newPhotoName);
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
