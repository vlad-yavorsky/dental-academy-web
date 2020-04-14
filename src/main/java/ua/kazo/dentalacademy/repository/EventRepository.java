package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
