package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kazo.dentalacademy.entity.EventUser;
import ua.kazo.dentalacademy.entity.EventUserId;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, EventUserId> {
}
