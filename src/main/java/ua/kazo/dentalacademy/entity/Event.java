package ua.kazo.dentalacademy.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.kazo.dentalacademy.constants.Graph;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NamedEntityGraph(
        name = Graph.EVENT_REGISTERED_USERS,
        attributeNodes = @NamedAttributeNode(value = "registeredUsers", subgraph = Graph.EVENT_REGISTERED_USERS_USER),
        subgraphs = @NamedSubgraph(name = Graph.EVENT_REGISTERED_USERS_USER, attributeNodes = @NamedAttributeNode("user"))
)
public class Event extends TrackedDateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_id_seq")
    @SequenceGenerator(name = "event_id_seq", sequenceName = "event_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String name;

    private String shortDescription;

    private String fullDescription;

    private String image;

    private LocalDateTime date;

    @ToString.Exclude
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventUser> registeredUsers = new ArrayList<>();

}
