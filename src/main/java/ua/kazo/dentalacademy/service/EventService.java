package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

}
