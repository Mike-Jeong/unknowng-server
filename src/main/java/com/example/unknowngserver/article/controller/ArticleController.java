package com.example.unknowngserver.article.controller;

import com.example.unknowngserver.article.dto.*;
import com.example.unknowngserver.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDetailInfo> getArticle(@PathVariable("id") Long id) {

        ArticleDto articleDto = articleService.getArticle(id);

        return ResponseEntity.ok(ArticleDetailInfo.fromArticleDto(articleDto));
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleBoardInfo>> getArticles(@RequestParam(defaultValue = "1", required = false) Integer page,
                                                              @RequestParam(defaultValue = "", required = false) String keyWord) {

        page = (page < 1) ? 1 : page;

        List<ArticleDto> articleDtoList = articleService.getArticles(page, keyWord);

        return ResponseEntity.ok(articleDtoList.stream()
                .map(ArticleBoardInfo::fromArticleDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/articles")
    public ResponseEntity<SubmitArticleResponse> submitArticle(
            @RequestBody @Valid SubmitArticleRequest submitArticleRequest) {

        ArticleDto articleDto = articleService.createArticle(submitArticleRequest);

        return ResponseEntity.ok(SubmitArticleResponse.fromArticleDto(articleDto));
    }

    @DeleteMapping("/articles")
    public ResponseEntity<Boolean> deleteArticle(
            @RequestBody @Valid DeleteArticleRequest deleteArticleRequest) {

        return ResponseEntity.ok(articleService.deleteArticle(deleteArticleRequest));
    }
}
