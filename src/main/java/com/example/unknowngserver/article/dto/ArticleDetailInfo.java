package com.example.unknowngserver.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ArticleDetailInfo {
    private Long id;
    private String author;
    private String title;
    private String content;
    private LocalDate createDate;

    public static ArticleDetailInfo fromArticleDto(ArticleDto articleDto) {
        return ArticleDetailInfo.builder()
                .id(articleDto.getId())
                .author(articleDto.getAuthor())
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .createDate(articleDto.getRegisteredAt().toLocalDate())
                .build();
    }
}