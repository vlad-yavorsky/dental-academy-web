package ua.kazo.dentalacademy.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.mapper.EventMapper;
import ua.kazo.dentalacademy.service.EventService;
import ua.kazo.dentalacademy.util.AuthUtils;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/events")
    public String events(final ModelMap model, final Principal principal, @RequestParam(required = false) final String search,
                          @RequestParam(defaultValue = "0") final int page,
                          @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Event> pageResult = eventService.findAllFutureEventsOrderByDate(search, PageRequest.of(page, size));
        model.addAttribute(ModelMapConstants.EVENTS, eventMapper.toUserRegisteredResponseDto(pageResult, AuthUtils.getUser(principal).getId()));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "client/event/events";
    }

    @GetMapping({"/event/{eventId}",})
    public String event(final @PathVariable Long eventId, final ModelMap model, final Principal principal) {
        Event event = eventService.findByIdFetchRegisteredUsers(eventId);
        model.addAttribute(ModelMapConstants.EVENT, eventMapper.toUserRegisteredResponseDto(event, AuthUtils.getUser(principal).getId()));
        return "client/event/event";
    }

    @GetMapping({"/event/{eventId}/register",})
    public RedirectView register(final @PathVariable Long eventId, final RedirectAttributes redirectAttributes, final Principal principal) {
        boolean registered = eventService.register(eventId, AuthUtils.getUser(principal).getId(), false);
        if (registered) {
            redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.event.registered");
        } else {
            redirectAttributes.addFlashAttribute(ModelMapConstants.ERRORS, "exception.event.NotFound");
        }
        return new RedirectView("/event/" + eventId);
    }

    @GetMapping({"/event/{eventId}/unregister",})
    public RedirectView unregister(final @PathVariable Long eventId, final RedirectAttributes redirectAttributes, final Principal principal) {
        boolean registered = eventService.register(eventId, AuthUtils.getUser(principal).getId(), true);
        if (registered) {
            redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.event.unregistered");
        } else {
            redirectAttributes.addFlashAttribute(ModelMapConstants.ERRORS, "exception.event.NotFound");
        }
        return new RedirectView("/event/" + eventId);
    }

}
