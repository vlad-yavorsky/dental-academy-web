package ua.kazo.dentalacademy.entity;

import lombok.*;
import ua.kazo.dentalacademy.enumerated.FolderItemType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    private String link;

    @Enumerated(EnumType.STRING)
    private FolderItemType type;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Folder folder;

    private int ordering;

    @ToString.Exclude
    @OneToMany(mappedBy = "folderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ViewedFolderItem> viewedFolderItems = new ArrayList<>();

}
