package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.entity.EventUser;
import ua.kazo.dentalacademy.entity.User;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.EventRepository;
import ua.kazo.dentalacademy.repository.EventUserRepository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventUserRepository eventUserRepository;
    private final MessageSource messageSource;
    private final EmailService emailService;

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.EVENT_NOT_FOUND, id));
    }

    public Event findByIdFetchRegisteredUsers(Long id) {
        return eventRepository.findFetchRegisteredUsersById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.EVENT_NOT_FOUND, id));
    }

    public Event findFetchRegisteredUsersWithUserById(Long id) {
        return eventRepository.findFetchRegisteredUsersWithUserById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.EVENT_NOT_FOUND, id));
    }

    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Page<Event> findAllFutureEventsOrderByDate(String search, Pageable pageable) {
        if (!ObjectUtils.isEmpty(search)) {
            return eventRepository.findAllByNameAndDateAfterOrderByDate(search, pageable, LocalDateTime.now());
        }
        return eventRepository.findAllByDateAfterOrderByDate(pageable, LocalDateTime.now());
    }

    public boolean register(Long eventId, User user, boolean unregister) {
        Optional<Event> event = eventRepository.findByIdAndDateAfter(eventId, LocalDateTime.now());
        if (event.isPresent()) {
            if (unregister) {
                eventUserRepository.delete(EventUser.of(event.get().getId(), user.getId()));
            } else {
                eventUserRepository.save(EventUser.of(event.get().getId(), user.getId()));
                emailService.sendUserRegisteredForEvent(user, event.get());
            }
            return true;
        }
        return false;
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public boolean existsByName(String name) {
        return eventRepository.existsByName(name);
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return eventRepository.existsByNameAndIdNot(name, id);
    }

}
