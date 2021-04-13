package ua.kazo.dentalacademy.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ua.kazo.dentalacademy.dto.user.UserUpdateDto;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.service.storage.FileSystemStorageService;
import ua.kazo.dentalacademy.service.storage.FileUploadController;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Autowired
    private FileSystemStorageService storageService;

    @Override
    public UserUpdateDto toUpdateDto(User user) {
        UserUpdateDto dto = delegate.toUpdateDto(user);
        if (!StringUtils.isEmpty(user.getPhotoName())) {
            dto.setExistingPhotoPath(MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "serveFile",
                            storageService.load(user.getPhotoName()).getFileName().toString())
                    .build()
                    .toUri()
                    .toString());
        }
        return dto;
    }

}
