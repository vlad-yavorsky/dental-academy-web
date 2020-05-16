package ua.kazo.dentalacademy.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.user.UserCreateDto;
import ua.kazo.dentalacademy.dto.user.UserPasswordUpdateDto;
import ua.kazo.dentalacademy.dto.user.UserUpdateDto;
import ua.kazo.dentalacademy.entity.Order;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.mapper.OrderMapper;
import ua.kazo.dentalacademy.mapper.UserMapper;
import ua.kazo.dentalacademy.service.OrderService;
import ua.kazo.dentalacademy.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    private void validateUserEmail(final User user, final BindingResult bindingResult, final boolean isRegister) {
        if (isRegister) {
            if (userService.existsByEmail(user.getEmail())) {
                bindingResult.rejectValue("deactivated", "validation.offering.IncorrectDates");
            }
        } else {
            if (userService.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
                bindingResult.rejectValue("deactivated", "validation.offering.IncorrectDates");
            }
        }
    }

    /* ---------------------------------------------- LOGIN ---------------------------------------------- */

    @GetMapping("/login")
    public String login(@RequestParam(defaultValue = "false") final boolean error) {
        return "client/user/login";
    }

    /* ---------------------------------------------- REGISTER ---------------------------------------------- */

    private String loadRegisterPage(final UserCreateDto userCreateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.USER, userCreateDto);
        return "client/user/register";
    }

    @GetMapping("/register")
    public String register(final ModelMap model) {
        return loadRegisterPage(new UserCreateDto(), model);
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(ModelMapConstants.USER) @Valid final UserCreateDto userCreateDto,
                           final BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttributes) {
        User user = userMapper.toEntity(userCreateDto);

        validateUserEmail(user, bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadRegisterPage(userCreateDto, model);
        }
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.user.add");

        userService.create(user);
        return "redirect:/login";
    }

    /* ---------------------------------------------- PROFILE UPDATE ---------------------------------------------- */

    private String loadUpdatePage(final UserUpdateDto userUpdateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.USER, userUpdateDto);
        return "client/user/profile";
    }

    @GetMapping("/profile")
    public String profile(final ModelMap model, final Principal principal) {
        model.addAttribute(ModelMapConstants.USER, userMapper.toUpdateDto(userService.findByEmailFetchRoles(principal.getName())));
        return "client/user/profile";
    }

    @PostMapping("/profile")
    public String updateUser(@ModelAttribute(ModelMapConstants.USER) @Valid final UserUpdateDto userUpdateDto, final BindingResult bindingResult, final ModelMap model, final Principal principal) {
        User user = userMapper.toEntity(userUpdateDto);

        validateUserEmail(user, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadUpdatePage(userUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.user.edit");

        User savedUser = userService.update(user, principal.getName());
        return loadUpdatePage(userMapper.toUpdateDto(savedUser), model);
    }

    /* ---------------------------------------------- PASSWORD CHANGE ---------------------------------------------- */

    private void validateUserPassword(final User userFromDb, final UserPasswordUpdateDto userPasswordUpdateDto, final BindingResult bindingResult) {
        if (!userService.arePasswordsMatches(userPasswordUpdateDto.getOldPassword(), userFromDb.getPassword())) {
            bindingResult.rejectValue("oldPassword", "validation.user.WrongOldPassword");
        }
        if (!userPasswordUpdateDto.getNewPassword().equals(userPasswordUpdateDto.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "validation.user.WrongConfirmPassword");
        }
    }

    private String loadChangePasswordPage(final ModelMap model) {
        model.addAttribute(ModelMapConstants.PASSWORD, new UserPasswordUpdateDto());
        return "client/user/password";
    }

    @GetMapping("/profile/password")
    public String changePassword(final ModelMap model) {
        return loadChangePasswordPage(model);
    }

    @PostMapping("/profile/password")
    public String changePassword(@ModelAttribute(ModelMapConstants.PASSWORD) @Valid final UserPasswordUpdateDto userPasswordUpdateDto, final BindingResult bindingResult, final ModelMap model, final Principal principal) {
        User userFromDb = userService.findByEmailFetchRoles(principal.getName());

        validateUserPassword(userFromDb, userPasswordUpdateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadChangePasswordPage(model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.user.password.edit");

        userService.updatePassword(userFromDb, userPasswordUpdateDto.getNewPassword());
        return loadChangePasswordPage(model);
    }

    /* ---------------------------------------------- PURCHASES ---------------------------------------------- */

    @GetMapping("/orders")
    public String orders(final ModelMap model, final Principal principal,
                         @RequestParam(defaultValue = "0") final int page,
                         @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Order> pageResult = orderService.findAllByUserEmail(principal.getName(), PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.ORDERS, orderMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "client/user/orders";
    }

}
