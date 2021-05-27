package ua.kazo.dentalacademy.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.kazo.dentalacademy.constants.AppConfig;
import ua.kazo.dentalacademy.constants.ModelMapConstants;
import ua.kazo.dentalacademy.dto.article.ArticleCreateDto;
import ua.kazo.dentalacademy.dto.article.ArticleUpdateDto;
import ua.kazo.dentalacademy.entity.Article;
import ua.kazo.dentalacademy.mapper.ArticleMapper;
import ua.kazo.dentalacademy.service.ArticleService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    /* ---------------------------------------------- ARTICLES---------------------------------------------- */

    @GetMapping("/articles")
    public String articles(final ModelMap model,
                         @RequestParam(defaultValue = "0") final int page,
                         @RequestParam(defaultValue = AppConfig.Constants.DEFAULT_PAGE_SIZE_VALUE) final int size) {
        Page<Article> pageResult = articleService.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute(ModelMapConstants.ARTICLES, articleMapper.toResponseDto(pageResult));
        model.addAttribute(ModelMapConstants.PAGE_RESULT, pageResult);
        return "admin/article/articles";
    }

    /* ---------------------------------------------- ADD ARTICLE ---------------------------------------------- */

    private void validateArticleAlias(final Article article, final BindingResult bindingResult, final boolean isAdd) {
        if (isAdd) {
            if (articleService.existsByAlias(article.getAlias())) {
                bindingResult.rejectValue("alias", "validation.AliasNotUnique");
            }
        } else {
            if (articleService.existsByAliasAndIdNot(article.getAlias(), article.getId())) {
                bindingResult.rejectValue("alias", "validation.AliasNotUnique");
            }
        }
    }

    private String loadArticleAddPage(final ArticleCreateDto articleCreateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.ARTICLE, articleCreateDto);
        return "admin/article/article-add";
    }

    @GetMapping("/article/add")
    public String addArticle(final ModelMap model) {
        return loadArticleAddPage(new ArticleCreateDto(), model);
    }

    @PostMapping("/article/add")
    public String addArticle(@ModelAttribute(ModelMapConstants.ARTICLE) @Valid final ArticleCreateDto articleCreateDto,
                             final BindingResult bindingResult, final ModelMap model, final RedirectAttributes redirectAttributes) {
        Article article = articleMapper.toEntity(articleCreateDto);

        validateArticleAlias(article, bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.FIELD_ERRORS, bindingResult.getFieldErrors());
            return loadArticleAddPage(articleCreateDto, model);
        }
        redirectAttributes.addFlashAttribute(ModelMapConstants.SUCCESS, "success.article.add");

        Article savedArticle = articleService.save(article);
        return "redirect:/admin/article/edit/" + savedArticle.getId();
    }

    /* ---------------------------------------------- EDIT ARTICLE ---------------------------------------------- */

    private String loadArticleEditPage(final ArticleUpdateDto articleUpdateDto, final ModelMap model) {
        model.addAttribute(ModelMapConstants.ARTICLE, articleUpdateDto);
        return "admin/article/article-edit";
    }

    @GetMapping("/article/edit/{id}")
    public String editArticle(@PathVariable final Long id, final ModelMap model) {
        return loadArticleEditPage(articleMapper.toUpdateDto(articleService.findById(id)), model);
    }

    @PostMapping("/article/edit/{id}")
    public String editArticle(@ModelAttribute(ModelMapConstants.ARTICLE) @Valid final ArticleUpdateDto articleUpdateDto, final BindingResult bindingResult, final ModelMap model) {
        Article article = articleMapper.toEntity(articleUpdateDto);

        validateArticleAlias(article, bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ModelMapConstants.FIELD_ERRORS, bindingResult.getFieldErrors());
            return loadArticleEditPage(articleUpdateDto, model);
        }
        model.addAttribute(ModelMapConstants.SUCCESS, "success.article.edit");

        Article savedArticle = articleService.save(article);
        return loadArticleEditPage(articleMapper.toUpdateDto(savedArticle), model);
    }

}
