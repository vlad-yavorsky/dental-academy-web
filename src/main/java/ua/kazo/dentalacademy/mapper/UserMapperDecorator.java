package ua.kazo.dentalacademy.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import ua.kazo.dentalacademy.dto.user.UserUpdateDto;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.service.storage.StorageService;

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

}
