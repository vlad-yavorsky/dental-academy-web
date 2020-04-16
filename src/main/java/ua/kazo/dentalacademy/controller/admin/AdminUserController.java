package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.mapper.UserMapper;
import ua.kazo.dentalacademy.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /* ---------------------------------------------- USERS ---------------------------------------------- */

    @GetMapping("/users")
    public String users(final ModelMap model) {
        model.addAttribute(ModelMapConstants.USERS, userMapper.toResponseDto(userService.findAll()));
        return "admin/user/users";
    }

}
