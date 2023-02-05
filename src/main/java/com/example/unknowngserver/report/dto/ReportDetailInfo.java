package com.example.unknowngserver.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDetailInfo {
    private Long reportId;
    private String reportedContentType;
    private Long targetId;
    private String targetContent;
    private int reportedCount;
    private LocalDateTime firstReportedAt;

    public static ReportDetailInfo fromReportDetailDto(ReportDetailDto reportDetailDto){
        return ReportDetailInfo.builder()
                .reportId(reportDetailDto.getReportId())
                .reportedContentType(reportDetailDto.getReportedContentType())
                .targetId(reportDetailDto.getTargetId())
                .targetContent(reportDetailDto.getTargetContent())
                .reportedCount(reportDetailDto.getReportedCount())
                .firstReportedAt(reportDetailDto.getFirstReportedAt())
                .build();
    }
}
