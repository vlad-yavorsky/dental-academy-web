package ua.kazo.dentalacademy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Event;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByDateAfterOrderByDate(Pageable pageable, LocalDateTime date);

    @Query("select distinct e from Event e " +
            "where (:date < e.date) " +
            "and lower(e.name) like lower(concat('%', concat(:search, '%'))) " +
            "order by e.date asc")
    Page<Event> findAllByNameAndDateAfterOrderByDate(String search, Pageable pageable, LocalDateTime date);

}
