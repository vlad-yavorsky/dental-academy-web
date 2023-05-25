package ua.kazo.dentalacademy.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class EventUser extends TrackedDateEntity implements Serializable {

    @EmbeddedId
    private EventUserId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("eventId")
    @ToString.Exclude
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @ToString.Exclude
    private User user;

    private EventUser(Event event, User user) {
        this.id = new EventUserId(event.getId(), user.getId());
        this.event = event;
        this.user = user;
    }

    public static EventUser of(Long eventId, Long userId) {
        Event event = new Event();
        event.setId(eventId);
        User user = new User();
        user.setId(userId);
        return new EventUser(event, user);
    }

}
