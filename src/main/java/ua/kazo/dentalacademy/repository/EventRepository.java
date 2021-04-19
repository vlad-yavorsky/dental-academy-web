package ua.kazo.dentalacademy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.Event;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = "registeredUsers")
    Optional<Event> findFetchRegisteredUsersById(Long id);

    Optional<Event> findByIdAndDateAfter(Long id, LocalDateTime date);

    @EntityGraph(attributePaths = "registeredUsers")
    Page<Event> findAllByDateAfterOrderByDate(Pageable pageable, LocalDateTime date);

    @EntityGraph(attributePaths = "registeredUsers")
    @Query("select distinct e from Event e " +
            "where (:date < e.date) " +
            "and lower(e.name) like lower(concat('%', concat(:search, '%'))) " +
            "order by e.date asc")
    Page<Event> findAllByNameAndDateAfterOrderByDate(String search, Pageable pageable, LocalDateTime date);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

}
