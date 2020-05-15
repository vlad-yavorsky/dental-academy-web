package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.entity.Event;
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
    public String events(final ModelMap model,
                         @RequestParam(defaultValue = "0") final int page,
                         @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Event> pageResult = eventService.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.EVENTS, eventMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "admin/event/events";
    }

}
