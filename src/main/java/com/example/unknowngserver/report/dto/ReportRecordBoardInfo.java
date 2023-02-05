package com.example.unknowngserver.report.dto;

import com.example.unknowngserver.report.type.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRecordBoardInfo {
    private ReportType reportType;
    private String memo;
    private LocalDateTime reportedDt;

    public static ReportRecordBoardInfo fromReportRecordDto(ReportRecordDto reportRecordDto) {
        return ReportRecordBoardInfo.builder()
                .reportType(reportRecordDto.getReportType())
                .memo(reportRecordDto.getMemo())
                .reportedDt(reportRecordDto.getReportedDt())
                .build();
    }
}
