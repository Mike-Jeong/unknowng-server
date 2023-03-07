package com.example.unknowngserver.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubmitArticleResponse {

    private Long articleId;

    public SubmitArticleResponse(Long articleId) {
        this.articleId = articleId;
    }

    public static SubmitArticleResponse fromArticleDto(ArticleDto articleDto) {
        return new SubmitArticleResponse(articleDto.getId());
    }
}
