package com.example.unknowngserver.comment.entity;

import com.example.unknowngserver.article.entity.Article;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@SQLDelete(sql = "UPDATE comment SET deleted = true, blockedAt = current_timestamp WHERE id = ? ")
@Where(clause = "deleted = false")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String author;
    private String password;
    private LocalDateTime registeredAt;

    @Builder.Default
    private boolean isBlocked = false;
    private LocalDateTime blockedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    public void addArticle(Article article) {
        this.article = article;
    }
}