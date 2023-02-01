package com.example.unknowngserver.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitArticleResponse {

    private Long articleId;

    public static SubmitArticleResponse fromArticleDto(ArticleDto articleDto) {
        return SubmitArticleResponse.builder()
                .articleId(articleDto.getId())
                .build();
    }
}
