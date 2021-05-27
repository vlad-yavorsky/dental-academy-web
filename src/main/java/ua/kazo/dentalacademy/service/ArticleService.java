package ua.kazo.dentalacademy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kazo.dentalacademy.entity.Article;
import ua.kazo.dentalacademy.enumerated.ExceptionCode;
import ua.kazo.dentalacademy.exception.ApplicationException;
import ua.kazo.dentalacademy.repository.ArticleRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MessageSource messageSource;

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.ARTICLE_NOT_FOUND, id));
    }

    public Article findByAlias(String alias) {
        return articleRepository.findByAlias(alias)
                .orElseThrow(() -> new ApplicationException(messageSource, ExceptionCode.ARTICLE_NOT_FOUND, alias));
    }

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public boolean existsByAlias(String alias) {
        return articleRepository.existsByAlias(alias);
    }

    public boolean existsByAliasAndIdNot(String alias, Long id) {
        return articleRepository.existsByAliasAndIdNot(alias, id);
    }

}
