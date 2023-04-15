package com.example.unknowngserver.article.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE article SET deleted = true, deleted_at = current_timestamp WHERE id = ?")
@Where(clause = "deleted = false")
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String author;
    private String password;
    @CreatedDate
    private LocalDateTime registeredAt;
    private boolean deleted;
    private LocalDateTime deletedAt;

    @Builder
    public Article(Long id, String title, String content, String author, String password, LocalDateTime registeredAt, boolean deleted, LocalDateTime deletedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.password = password;
        this.registeredAt = registeredAt;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }
}
