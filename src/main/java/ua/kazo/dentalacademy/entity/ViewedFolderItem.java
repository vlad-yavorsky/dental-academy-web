package ua.kazo.dentalacademy.entity;

import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"user", "folderItem"}, callSuper = false)
public class ViewedFolderItem implements Serializable {

    @EmbeddedId
    private ViewedFolderItemId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("folderItemId")
    @ToString.Exclude
    private FolderItem folderItem;

    private LocalDateTime lastView;

    private ViewedFolderItem(User user, FolderItem folderItem) {
        this.user = user;
        this.folderItem = folderItem;
        this.lastView = LocalDateTime.now();
        this.id = new ViewedFolderItemId(user.getId(), folderItem.getId());
    }

    public static ViewedFolderItem of(Long userId, Long folderItemId) {
        User user = new User();
        user.setId(userId);
        FolderItem folderItem = new FolderItem();
        folderItem.setId(folderItemId);
        return new ViewedFolderItem(user, folderItem);
    }

}
