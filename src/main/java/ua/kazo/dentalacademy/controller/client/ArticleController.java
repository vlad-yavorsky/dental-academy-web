package ua.kazo.dentalacademy.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.entity.Article;
import ua.kazo.dentalacademy.mapper.ArticleMapper;
import ua.kazo.dentalacademy.service.ArticleService;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @GetMapping("/article/{alias}")
    public String articles(@PathVariable final String alias, final ModelMap model) {
        Article article = articleService.findByAlias(alias);
        model.addAttribute(ModelMapConstants.ARTICLE, articleMapper.toResponseDto(article));
        return "client/article/article";
    }

}
