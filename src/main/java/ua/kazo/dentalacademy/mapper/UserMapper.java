package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import ua.kazo.dentalacademy.dto.user.UserCreateDto;
import ua.kazo.dentalacademy.dto.user.UserResponseDto;
import ua.kazo.dentalacademy.dto.user.UserUpdateDto;
import ua.kazo.dentalacademy.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    User toEntity(UserCreateDto userCreateDto);
    User toEntity(UserUpdateDto userUpdateDto);

    UserUpdateDto toUpdateDto(User user);

    UserResponseDto toResponseDto(User user);
    List<UserResponseDto> toResponseDto(List<User> users);

}
