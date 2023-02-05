package com.example.unknowngserver.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportBoardInfo {
    private Long reportId;
    private String reportedContentType;
    private Integer reportedCount;
    private LocalDate firstReportedAt;

    public static ReportBoardInfo fromReportDto(ReportDto reportDto){
        return ReportBoardInfo.builder()
                .reportId(reportDto.getReportId())
                .reportedContentType(reportDto.getReportedContentType())
                .reportedCount(reportDto.getReportedCount())
                .firstReportedAt(reportDto.getFirstReportedAt().toLocalDate())
                .build();
    }
}
