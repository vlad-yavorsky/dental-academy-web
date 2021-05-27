package ua.kazo.dentalacademy.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ua.kazo.dentalacademy.dto.article.ArticleCreateDto;
import ua.kazo.dentalacademy.dto.article.ArticleResponseDto;
import ua.kazo.dentalacademy.dto.article.ArticleUpdateDto;
import ua.kazo.dentalacademy.entity.Article;

import java.util.List;

@Mapper
public interface ArticleMapper {

    Article toEntity(ArticleCreateDto dto);
    Article toEntity(ArticleUpdateDto dto);

    ArticleUpdateDto toUpdateDto(Article article);
    ArticleResponseDto toResponseDto(Article article);
    List<ArticleResponseDto> toResponseDto(Page<Article> article);

}
