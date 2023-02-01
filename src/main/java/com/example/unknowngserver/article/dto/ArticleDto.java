package com.example.unknowngserver.article.dto;

import com.example.unknowngserver.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime registeredAt;

    public static ArticleDto fromEntity(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .registeredAt(article.getRegisteredAt())
                .build();
    }
}
