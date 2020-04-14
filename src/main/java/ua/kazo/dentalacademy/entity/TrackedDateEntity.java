package ua.kazo.dentalacademy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter // for MapStruct
@Setter // for MapStruct
public class TrackedDateEntity {

    @Column(insertable = false, updatable = false)
    private LocalDateTime created;

}
