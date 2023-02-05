package com.example.unknowngserver.report.dto;

import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.entity.ReportComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDetailDto {
    private Long reportId;
    private String reportedContentType;
    private Long targetId;
    private String targetContent;
    private int reportedCount;
    private LocalDateTime firstReportedAt;

    public static ReportDetailDto fromReportArticleEntity(ReportArticle reportArticle) {
        return ReportDetailDto.builder()
                .reportId(reportArticle.getId())
                .reportedContentType(reportArticle.getContentType())
                .targetId(reportArticle.getArticle().getId())
                .reportedCount(reportArticle.getReportedCount())
                .firstReportedAt(reportArticle.getFirstReportedAt())
                .build();
    }

    public static ReportDetailDto fromReportCommentEntity(ReportComment reportComment) {
        return ReportDetailDto.builder()
                .reportId(reportComment.getId())
                .reportedContentType(reportComment.getContentType())
                .targetId(reportComment.getComment().getId())
                .reportedCount(reportComment.getReportedCount())
                .firstReportedAt(reportComment.getFirstReportedAt())
                .build();
    }
}

