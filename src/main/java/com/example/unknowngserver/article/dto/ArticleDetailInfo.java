package com.example.unknowngserver.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ArticleDetailInfo {
    private Long id;
    private String author;
    private String title;
    private String content;
    private LocalDate createDate;

    @Builder
    public ArticleDetailInfo(Long id, String author, String title, String content, LocalDate createDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
    }

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
