package com.example.unknowngserver.article.entity;

import com.example.unknowngserver.comment.entity.Comment;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.test.entity.ReportArticleTest;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@SQLDelete(sql = "UPDATE article SET deleted = true, blockedAt = current_timestamp WHERE id = ?")
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

    @Builder.Default
    private boolean deleted = false;
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comment.addArticle(this);
        this.comments.add(comment);
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.REMOVE)
    private ReportArticle reportArticle;

    public void addArticleReport(ReportArticle reportArticle) {
        reportArticle.addArticle(this);
        this.reportArticle = reportArticle;
    }
}
