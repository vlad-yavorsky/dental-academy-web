package ua.kazo.dentalacademy.dto.article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleUpdateDto {

    private Long id;
    private String name;
    private String alias;
    private String description;

}
