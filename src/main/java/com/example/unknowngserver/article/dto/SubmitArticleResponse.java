package com.example.unknowngserver.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubmitArticleResponse {

    private Long articleId;

    @Builder
    public SubmitArticleResponse(Long articleId) {
        this.articleId = articleId;
    }

    public static SubmitArticleResponse fromArticleDto(ArticleDto articleDto) {
        return SubmitArticleResponse.builder()
                .articleId(articleDto.getId())
                .build();
    }
}
