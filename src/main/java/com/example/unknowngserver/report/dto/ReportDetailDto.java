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
}

