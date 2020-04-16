package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.mapper.EventMapper;
import ua.kazo.dentalacademy.service.EventService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    /* ---------------------------------------------- EVENTS ---------------------------------------------- */

    @GetMapping("/events")
    public String events(final ModelMap model) {
        model.addAttribute(ModelMapConstants.EVENTS, eventMapper.toResponseDto(eventService.findAll()));
        return "admin/event/events";
    }

}
