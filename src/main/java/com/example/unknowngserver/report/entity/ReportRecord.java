package com.example.unknowngserver.report.entity;

import com.example.unknowngserver.report.type.ReportType;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@SQLDelete(sql = "UPDATE report_record SET isHandled = true WHERE id = ? ")
@Where(clause = "isHandled = false")
public class ReportRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Report report;
    private ReportType reportType;
    private String memo;
    private LocalDateTime reportedDt;

    @Builder.Default
    private boolean isHandled = Boolean.FALSE;

    public void addReport(Report report) {
        this.report = report;
    }
}
