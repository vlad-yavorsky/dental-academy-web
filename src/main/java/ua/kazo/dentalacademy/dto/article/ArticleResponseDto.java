package ua.kazo.dentalacademy.dto.article;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleResponseDto {

    private Long id;
    private LocalDateTime created;
    private String name;
    private String alias;
    private String description;

}
