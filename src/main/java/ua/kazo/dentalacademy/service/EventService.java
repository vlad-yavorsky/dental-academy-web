package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.EventRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final MessageSource messageSource;

    public Event findById(Long id) {
        return eventRepository.findById(id)
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

}
