package com.example.unknowngserver.report.service;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.exception.ArticleException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.entity.Report;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.entity.ReportRecord;
import com.example.unknowngserver.report.repository.ReportArticleRepository;
import com.example.unknowngserver.report.repository.ReportRecordRepository;
import com.example.unknowngserver.report.type.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportArticleService implements ReportContentService {

    private final ReportArticleRepository reportArticleRepository;
    private final ReportRecordRepository reportRecordRepository;
    private final ArticleRepository articleRepository;

    @Override
    @Transactional
    public void createReport(SubmitReportRequest submitReportRequest) {

        Article article = findArticle(submitReportRequest.getContentId());
        LocalDateTime currentTimeStamp = LocalDateTime.now();

        ReportArticle reportArticle = reportArticleRepository.findByArticle(article)
                .orElseGet(() -> reportArticleRepository.save(ReportArticle.builder()
                        .firstReportedAt(currentTimeStamp)
                        .article(article)
                        .build()));

        reportRecordRepository.save(ReportRecord.builder()
                .report(reportArticle)
                .reportType(ReportType.valueOf(submitReportRequest.getReportType()))
                .memo(submitReportRequest.getMemo())
                .reportedDt(currentTimeStamp)
                .build());

        reportArticle.updateReportedCount(reportRecordRepository.countReportRecordByReport(reportArticle));

    }

    @Override
    @Transactional
    public void deleteReport(Report report) {

        ReportArticle reportArticle = (ReportArticle) report;

        articleRepository.delete(reportArticle.getArticle());
        reportArticleRepository.delete(reportArticle);
    }

    @Override
    public ReportDetailDto getReportDetail(Report report) {

        ReportArticle reportArticle = (ReportArticle) report;

        return ReportDetailDto.builder()
                .reportId(reportArticle.getId())
                .reportedContentType(reportArticle.getContentType())
                .targetId(reportArticle.getArticle().getId())
                .targetContent(reportArticle.getArticle().getContent())
                .reportedCount(reportArticle.getReportedCount())
                .firstReportedAt(reportArticle.getFirstReportedAt())
                .build();

    }

    private Article findArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.ARTICLE_NOT_FOUND));
    }

}
