package com.example.unknowngserver.report.entity;

import com.example.unknowngserver.article.entity.Article;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
@Entity
@DiscriminatorValue("ARTICLE")
@SQLDelete(sql = "UPDATE report SET processed = true, processed_at = current_timestamp WHERE id = ? ")
public class ReportArticle extends Report {
    @OneToOne(fetch = FetchType.LAZY)
    private Article article;
}
