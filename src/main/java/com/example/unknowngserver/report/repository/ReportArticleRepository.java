package com.example.unknowngserver.report.repository;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.report.entity.Report;
import com.example.unknowngserver.report.entity.ReportArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportArticleRepository extends JpaRepository<ReportArticle, Long> {
    Optional<ReportArticle> findByArticle(Article article);
}
