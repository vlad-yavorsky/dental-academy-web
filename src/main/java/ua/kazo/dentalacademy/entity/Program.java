package ua.kazo.dentalacademy.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Program extends TrackedDateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_id_seq")
    @SequenceGenerator(name = "program_id_seq", sequenceName = "program_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private String image;

    @ManyToMany(mappedBy = "programs")
    private List<Offering> offerings;

    @ManyToMany(mappedBy = "programs")
    private List<Folder> folders;

}
