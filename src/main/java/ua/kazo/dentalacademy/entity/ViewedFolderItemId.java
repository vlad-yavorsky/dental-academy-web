package ua.kazo.dentalacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewedFolderItemId implements Serializable {

    private Long userId;

    private Long folderItemId;

}
