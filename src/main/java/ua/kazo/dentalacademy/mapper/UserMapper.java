package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.purchase.UserCartDto;
import ua.kazo.dentalacademy.dto.user.UserCreateDto;
import ua.kazo.dentalacademy.dto.user.UserResponseDto;
import ua.kazo.dentalacademy.dto.user.UserUpdateDto;
import ua.kazo.dentalacademy.entity.Offering;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.util.MathUtil;

import java.math.BigDecimal;
import java.util.List;

@Mapper(uses = OfferingMapper.class)
public interface UserMapper {

    User toEntity(UserCreateDto userCreateDto);
    User toEntity(UserUpdateDto userUpdateDto);

    UserUpdateDto toUpdateDto(User user);

    UserResponseDto toResponseDto(User user);
    List<UserResponseDto> toResponseDto(Page<User> users);

    @Mapping(target = "totalPrice", source = "cartItems", qualifiedByName = "calculateTotalPrice")
    @Mapping(target = "totalDiscountPrice", source = "cartItems", qualifiedByName = "calculateTotalDiscountPrice")
    UserCartDto toCartDto(User user);

    @Named("calculateTotalPrice")
    default BigDecimal calculateTotalPrice(List<Offering> cartItems) {
        if (cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return cartItems.stream()
                .map(Offering::getPrice)
                .reduce(BigDecimal::add)
                .get();
    }

    @Named("calculateTotalDiscountPrice")
    default BigDecimal calculateTotalDiscountPrice(List<Offering> cartItems) {
        if (cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return cartItems.stream()
                .map(offering -> MathUtil.calculateDiscountPrice(offering.getPrice(), offering.getDiscount()))
                .reduce(BigDecimal::add)
                .get();
    }

}
