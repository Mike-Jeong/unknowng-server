package com.example.unknowngserver.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReportDetailDto {
    private Long reportId;
    private String reportedContentType;
    private Long targetId;
    private String targetContent;
    private int reportedCount;
    private LocalDateTime firstReportedAt;

    @Builder
    public ReportDetailDto(Long reportId, String reportedContentType, Long targetId, String targetContent, int reportedCount, LocalDateTime firstReportedAt) {
        this.reportId = reportId;
        this.reportedContentType = reportedContentType;
        this.targetId = targetId;
        this.targetContent = targetContent;
        this.reportedCount = reportedCount;
        this.firstReportedAt = firstReportedAt;
    }
}

