package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.user.UserFullUpdateDto;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.mapper.UserMapper;
import ua.kazo.dentalacademy.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /* ---------------------------------------------- USERS ---------------------------------------------- */

    @GetMapping("/users")
    public String users(final ModelMap model,
                        @RequestParam(defaultValue = "0") final int page,
                        @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<User> pageResult = userService.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.USERS, userMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "admin/user/users";
    }

    /* ---------------------------------------------- EDIT USER ---------------------------------------------- */

    private String loadUserEditPage(final UserFullUpdateDto userFullUpdateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.USER, userFullUpdateDto);
        return "admin/user/user-edit";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Long id, final ModelMap model) {
        return loadUserEditPage(userMapper.toFullUpdateDto(userService.findByIdFetchRoles(id)), model);
    }

    @PostMapping("/user/edit/{id}")
    public String editUser(@ModelAttribute(ModelMapConstants.USER) @Valid final UserFullUpdateDto userFullUpdateDto,
                           @PathVariable Long id, final BindingResult bindingResult, final ModelMap model) {
        User user = userMapper.toEntity(userFullUpdateDto);

        userService.validateUserEmail(user, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.FIELD_ERRORS, bindingResult.getFieldErrors());
            return loadUserEditPage(userFullUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.user.edit");

        User savedUser = userService.update(user, userFullUpdateDto.getNewPhoto(), userFullUpdateDto.isRemoveExistingPhoto(), id);
        return loadUserEditPage(userMapper.toFullUpdateDto(savedUser), model);
    }

}
