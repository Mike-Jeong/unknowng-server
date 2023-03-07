package com.example.unknowngserver.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ArticleBoardInfo {
    private Long id;
    private String author;
    private String title;
    private LocalDate createDate;

    @Builder
    public ArticleBoardInfo(Long id, String author, String title, LocalDate createDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.createDate = createDate;
    }

    public static ArticleBoardInfo fromArticleDto(ArticleDto articleDto) {
        return ArticleBoardInfo.builder()
                .id(articleDto.getId())
                .author(articleDto.getAuthor())
                .title(articleDto.getTitle())
                .createDate(articleDto.getRegisteredAt().toLocalDate())
                .build();
    }
}
