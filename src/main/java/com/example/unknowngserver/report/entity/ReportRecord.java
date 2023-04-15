package com.example.unknowngserver.report.entity;

import com.example.unknowngserver.report.type.ReportType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ReportRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Report report;
    private ReportType reportType;
    private String memo;
    private LocalDateTime reportedDt;

    @Builder
    public ReportRecord(Long id, Report report, ReportType reportType, String memo, LocalDateTime reportedDt) {
        this.id = id;
        this.report = report;
        this.reportType = reportType;
        this.memo = memo;
        this.reportedDt = reportedDt;
    }
}
