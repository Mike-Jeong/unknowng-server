package com.example.unknowngserver.report.dto;

import com.example.unknowngserver.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {
    private Long reportId;
    private String reportedContentType;
    private Integer reportedCount;
    private LocalDateTime firstReportedAt;

    public static ReportDto fromEntity(Report report) {
        return ReportDto.builder()
                .reportId(report.getId())
                .reportedContentType(report.getContentType())
                .firstReportedAt(report.getFirstReportedAt())
                .reportedCount(report.getReportedCount())
                .build();
    }
}
