package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.entity.EventUser;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.EventRepository;
import ua.kazo.dentalacademy.repository.EventUserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventUserRepository eventUserRepository;
    private final MessageSource messageSource;

    public Event findByIdFetchRegisteredUsers(Long id) {
        return eventRepository.findFetchRegisteredUsersById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.EVENT_NOT_FOUND, id));
    }

    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Page<Event> findAllFutureEventsOrderByDate(String search, Pageable pageable) {
        if (!StringUtils.isEmpty(search)) {
            return eventRepository.findAllByNameAndDateAfterOrderByDate(search, pageable, LocalDateTime.now());
        }
        return eventRepository.findAllByDateAfterOrderByDate(pageable, LocalDateTime.now());
    }

    public boolean register(Long eventId, Long userId, boolean unregister) {
        Optional<Event> event = eventRepository.findByIdAndDateAfter(eventId, LocalDateTime.now());
        if (event.isPresent()) {
            if (unregister) {
                eventUserRepository.delete(EventUser.of(event.get().getId(), userId));
            } else {
                eventUserRepository.save(EventUser.of(event.get().getId(), userId));
            }
            return true;
        }
        return false;
    }

}
