package com.example.unknowngserver.article.repository;

import com.example.unknowngserver.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAll(Pageable pageable);
    Page<Article> findByTitleContains(Pageable pageable, String Keyword);
}
