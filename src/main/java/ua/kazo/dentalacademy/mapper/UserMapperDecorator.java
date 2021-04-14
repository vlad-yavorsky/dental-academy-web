package ua.kazo.dentalacademy.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import ua.kazo.dentalacademy.dto.user.UserResponseDto;
import ua.kazo.dentalacademy.dto.user.UserUpdateDto;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.service.storage.StorageService;

import java.util.List;
import java.util.stream.Collectors;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Autowired
    private StorageService storageService;

    @Override
    public UserUpdateDto toUpdateDto(User user) {
        UserUpdateDto dto = delegate.toUpdateDto(user);
        if (!StringUtils.isEmpty(user.getPhotoName())) {
            dto.setExistingPhotoPath(storageService.getPhotoLink(user.getPhotoName()));
        }
        return dto;
    }

    @Override
    public UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = delegate.toResponseDto(user);
        if (!StringUtils.isEmpty(user.getPhotoName())) {
            dto.setExistingPhotoPath(storageService.getPhotoLink(user.getPhotoName()));
        }
        return dto;
    }

    @Override
    public List<UserResponseDto> toResponseDto(Page<User> users) {
        return users.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

}
