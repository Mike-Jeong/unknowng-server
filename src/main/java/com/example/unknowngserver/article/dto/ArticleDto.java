package com.example.unknowngserver.article.dto;

import com.example.unknowngserver.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime registeredAt;

    @Builder
    public ArticleDto(Long id, String title, String content, String author, LocalDateTime registeredAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.registeredAt = registeredAt;
    }

    public static ArticleDto fromArticle(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .registeredAt(article.getRegisteredAt())
                .build();
    }
}
