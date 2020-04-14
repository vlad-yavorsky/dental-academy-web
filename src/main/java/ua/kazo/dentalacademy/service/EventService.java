package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.kazo.dentalacademy.entity.Event;
import ua.kazo.dentalacademy.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findAll(Sort.by("date"));
    }

}
