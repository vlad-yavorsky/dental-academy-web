package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.event.EventCreateDto;
import ua.kazo.dentalacademy.dto.event.EventUpdateDto;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.mapper.EventMapper;
import ua.kazo.dentalacademy.service.EventService;

import javax.validation.Valid;

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

    /* ---------------------------------------------- ADD EVENT ---------------------------------------------- */

    private void validateEventName(final Event event, final BindingResult bindingResult, final boolean isAdd) {
        if (isAdd) {
            if (eventService.existsByName(event.getName())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
        } else {
            if (eventService.existsByNameAndIdNot(event.getName(), event.getId())) {
                bindingResult.rejectValue("name", "validation.NameNotUnique");
            }
        }
    }

    private String loadEventAddPage(final EventCreateDto eventCreateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.EVENT, eventCreateDto);
        return "admin/event/event-add";
    }

    @GetMapping("/event/add")
    public String addEvent(final ModelMap model) {
        EventCreateDto eventCreateDto = new EventCreateDto();
        return loadEventAddPage(eventCreateDto, model);
    }

    @PostMapping("/event/add")
    public String addEvent(@ModelAttribute(ModelMapConstants.EVENT) @Valid final EventCreateDto eventCreateDto,
                              final BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttributes) {
        Event event = eventMapper.toEntity(eventCreateDto);

        validateEventName(event, bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadEventAddPage(eventCreateDto, model);
        }
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.event.add");

        Event savedEvent = eventService.save(event);
        return "redirect:/admin/event/edit/" + savedEvent.getId();
    }

    /* ---------------------------------------------- EDIT EVENT ---------------------------------------------- */

    private String loadEventEditPage(final EventUpdateDto eventUpdateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.EVENT, eventUpdateDto);
        return "admin/event/event-edit";
    }

    @GetMapping("/event/edit/{id}")
    public String editEvent(@PathVariable Long id, final ModelMap model) {
        return loadEventEditPage(eventMapper.toUpdateDto(eventService.findById(id)), model);
    }

    @PostMapping("/event/edit/{id}")
    public String editEvent(@ModelAttribute(ModelMapConstants.EVENT) @Valid final EventUpdateDto eventUpdateDto, final BindingResult bindingResult, final ModelMap model) {
        Event event = eventMapper.toEntity(eventUpdateDto);

        validateEventName(event, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.ERRORS, bindingResult.getFieldErrors());
            return loadEventEditPage(eventUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.event.edit");

        Event savedEvent = eventService.save(event);
        return loadEventEditPage(eventMapper.toUpdateDto(savedEvent), model);
    }

}
