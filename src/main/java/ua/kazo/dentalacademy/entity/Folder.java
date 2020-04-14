package ua.kazo.dentalacademy.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.FolderCategory;

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

    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    private FolderCategory category;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FolderItem> items;

    @ManyToMany(mappedBy = "folders")
    private List<Offering> offerings;

    @ManyToMany
    @JoinTable(name = "program_folder",
            joinColumns = {@JoinColumn(name = "folderId")},
            inverseJoinColumns = {@JoinColumn(name = "programId")})
    private List<Program> programs;

}
