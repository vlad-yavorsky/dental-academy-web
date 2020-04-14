package ua.kazo.dentalacademy.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.FolderItemType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class FolderItem extends TrackedDateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "folder_item_id_seq")
    @SequenceGenerator(name = "folder_item_id_seq", sequenceName = "folder_item_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String name;

    private String image;

    private String link;

    @Enumerated(EnumType.STRING)
    private FolderItemType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Folder folder;

    private int ordering;

}
