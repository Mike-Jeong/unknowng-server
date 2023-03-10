package com.example.unknowngserver.article.service;

import com.example.unknowngserver.article.dto.ArticleDto;
import com.example.unknowngserver.article.dto.DeleteArticleRequest;
import com.example.unknowngserver.article.dto.SubmitArticleRequest;
import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.common.dto.Keyword;
import com.example.unknowngserver.common.dto.PageNumber;
import com.example.unknowngserver.exception.ArticleException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.report.repository.ReportArticleRepository;
import com.example.unknowngserver.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private static final int PAGE_SIZE = 10;

    private final ArticleRepository articleRepository;
    private final ReportArticleRepository reportArticleRepository;
    private final PasswordUtil passwordUtil;

    public ArticleDto getArticle(Long id) {

        Article article = findArticle(id);

        ArticleDto articleDto = ArticleDto.fromArticle(article);

        return articleDto;
    }

    public List<ArticleDto> getArticles(PageNumber page, Keyword keyword) {

        PageRequest pageRequest = PageRequest.of(page.getPage(), PAGE_SIZE, Sort.by("id").descending());

        Page<Article> articlePageList = getArticlePage(pageRequest, keyword.getKeyword());

        return articlePageList.stream()
                .map(ArticleDto::fromArticle)
                .collect(Collectors.toList());
    }

    @Transactional
    public ArticleDto createArticle(SubmitArticleRequest submitArticleRequest) {

        String encPassword = passwordUtil.encodePassword(submitArticleRequest.getPassword());

        return ArticleDto.fromArticle(articleRepository.save(
                Article.builder()
                        .title(submitArticleRequest.getTitle())
                        .content(submitArticleRequest.getContent())
                        .author(submitArticleRequest.getAuthor())
                        .password(encPassword)
                        .registeredAt(LocalDateTime.now())
                        .build()));
    }

    @Transactional
    public void deleteArticle(DeleteArticleRequest deleteArticleRequest) {

        Article article = findArticle(deleteArticleRequest.getArticleId());

        passwordUtil.isPasswordValid(deleteArticleRequest.getPassword(), article.getPassword());

        reportArticleRepository.findByArticle(article).ifPresent(reportArticleRepository::delete);

        articleRepository.delete(article);
    }

    private Article findArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    private Page<Article> getArticlePage(PageRequest pageRequest, String keyword) {

        return StringUtils.isBlank(keyword) ? articleRepository.findAll(pageRequest)
                : articleRepository.findByTitleContains(pageRequest, keyword);
    }

}
