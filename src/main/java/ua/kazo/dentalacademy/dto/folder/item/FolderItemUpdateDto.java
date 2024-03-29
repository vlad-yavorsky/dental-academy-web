package ua.kazo.dentalacademy.dto.folder.item;

import lombok.Getter;
import lombok.Setter;
import ua.kazo.dentalacademy.enumerated.FolderItemType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class FolderItemUpdateDto {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String link;
    @NotNull
    private FolderItemType type;
    private Long folderId;
    private int ordering;

}
