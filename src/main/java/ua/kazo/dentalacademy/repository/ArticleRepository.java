package ua.kazo.dentalacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kazo.dentalacademy.entity.Article;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByAlias(String alias);

    boolean existsByAlias(String alias);

    boolean existsByAliasAndIdNot(String alias, Long id);

}
