package com.example.unknowngserver.report.dto;

import com.example.unknowngserver.report.entity.ReportRecord;
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
public class ReportRecordDto {
    private Long id;
    private ReportType reportType;
    private String memo;
    private LocalDateTime reportedDt;

    public static ReportRecordDto fromEntity(ReportRecord reportRecord) {
        return ReportRecordDto.builder()
                .id(reportRecord.getId())
                .reportType(reportRecord.getReportType())
                .memo(reportRecord.getMemo())
                .reportedDt(reportRecord.getReportedDt())
                .build();
    }
}
