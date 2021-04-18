package ua.kazo.dentalacademy.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.mapper.EventMapper;
import ua.kazo.dentalacademy.service.EventService;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/events")
    public String events(final ModelMap model, @RequestParam(required = false) final String search,
                          @RequestParam(defaultValue = "0") final int page,
                          @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Event> pageResult = eventService.findAllFutureEventsOrderByDate(search, PageRequest.of(page, size)); // todo: search
        model.addAttribute(ModelMapConstants.EVENTS, eventMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.SEARCH, search);
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "client/event/events";
    }

    @GetMapping({"/event/{eventId}",})
    public String event(final @PathVariable Long eventId, final ModelMap model) {
        Event event = eventService.findById(eventId);
        model.addAttribute(ModelMapConstants.EVENT, eventMapper.toResponseDto(event));
        model.addAttribute("isFutureDate", event.getDate().isAfter(LocalDateTime.now()));
        return "client/event/event";
    }

}
