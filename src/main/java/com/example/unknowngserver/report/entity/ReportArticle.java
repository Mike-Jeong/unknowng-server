package com.example.unknowngserver.report.entity;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.test.entity.ArticleTest;
import com.example.unknowngserver.test.entity.ReportTest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
@DiscriminatorValue("ARTICLE")
@SQLDelete(sql = "UPDATE report SET processed = true, processedAt = current_timestamp WHERE id = ? ")
public class ReportArticle extends Report {
    @OneToOne(fetch = FetchType.LAZY)
    private Article article;

    public void addArticle(Article article) {
        this.article = article;
    };
}
