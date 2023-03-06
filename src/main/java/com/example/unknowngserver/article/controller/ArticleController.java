package com.example.unknowngserver.article.controller;

import com.example.unknowngserver.article.dto.*;
import com.example.unknowngserver.article.service.ArticleService;
import com.example.unknowngserver.common.dto.Keyword;
import com.example.unknowngserver.common.dto.PageNumber;
import com.example.unknowngserver.common.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDetailInfo> getArticle(@PathVariable("id") Long id) {

        ArticleDto articleDto = articleService.getArticle(id);

        ArticleDetailInfo articleDetailInfo = ArticleDetailInfo.fromArticleDto(articleDto);

        return ResponseEntity.ok(articleDetailInfo);
    }

    @GetMapping
    public ResponseEntity<List<ArticleBoardInfo>> getArticles(@ModelAttribute PageNumber page,
                                                              @ModelAttribute Keyword keyword) {

        List<ArticleDto> articleDtoList = articleService.getArticles(page, keyword);

        List<ArticleBoardInfo> articleBoardInfoList = articleDtoList.stream()
                .map(ArticleBoardInfo::fromArticleDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(articleBoardInfoList);
    }

    @PostMapping
    public ResponseEntity<SubmitArticleResponse> submitArticle(@RequestBody @Valid SubmitArticleRequest submitArticleRequest) {

        ArticleDto articleDto = articleService.createArticle(submitArticleRequest);

        SubmitArticleResponse submitArticleResponse = SubmitArticleResponse.fromArticleDto(articleDto);

        return ResponseEntity.ok(submitArticleResponse);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteArticle(@RequestBody @Valid DeleteArticleRequest deleteArticleRequest) {

        articleService.deleteArticle(deleteArticleRequest);

        return ResponseEntity.ok().body(Response.ok());
    }
}
