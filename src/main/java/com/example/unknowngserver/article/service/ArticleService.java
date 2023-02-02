package com.example.unknowngserver.article.service;

import com.example.unknowngserver.article.dto.ArticleDto;
import com.example.unknowngserver.article.dto.DeleteArticleRequest;
import com.example.unknowngserver.article.dto.SubmitArticleRequest;
import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.exception.ArticleException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.repository.ReportArticleRepository;
import com.example.unknowngserver.report.repository.ReportRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ReportArticleRepository reportArticleRepository;

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long id) {

        Article article = findArticle(id);

        return ArticleDto.fromEntity(article);
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> getArticles(Integer page, String keyWord) {

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<Article> articlePageList = keyWord.isBlank() ? articleRepository.findAll(pageRequest)
                : articleRepository.findByTitleContains(pageRequest, keyWord);

        return articlePageList.stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ArticleDto createArticle(SubmitArticleRequest submitArticleRequest) {

        String encPassword = BCrypt.hashpw(submitArticleRequest.getPassword(), BCrypt.gensalt());

        return ArticleDto.fromEntity(articleRepository.save(
                Article.builder()
                        .title(submitArticleRequest.getTitle())
                        .content(submitArticleRequest.getContent())
                        .author(submitArticleRequest.getAuthor())
                        .password(encPassword)
                        .registeredAt(LocalDateTime.now())
                        .build()));
    }

    @Transactional
    public boolean deleteArticle(DeleteArticleRequest deleteArticleRequest) {

        Article article = findArticle(deleteArticleRequest.getArticleId());

        if (!BCrypt.checkpw(deleteArticleRequest.getPassword(), article.getPassword())) {
            throw new ArticleException(ErrorCode.NO_PERMISSION);
        }

        Optional<ReportArticle> reportArticle = reportArticleRepository.findByArticle(article);
        reportArticle.ifPresent(reportArticleRepository::delete);

        articleRepository.delete(article);
        return true;
    }

    private Article findArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.ARTICLE_NOT_FOUND));
    }
}
