package ua.kazo.dentalacademy.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter // for MapStruct
@Setter // for MapStruct
public class TrackedDateEntity {

    @Column(insertable = false, updatable = false) // value is set by database column default value
    private LocalDateTime created;

}
