package ua.kazo.dentalacademy.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Folder extends TrackedDateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "folder_id_seq")
    @SequenceGenerator(name = "folder_id_seq", sequenceName = "folder_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String name;

    private int ordering;

    @ToString.Exclude
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FolderItem> items;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Program program;

}
