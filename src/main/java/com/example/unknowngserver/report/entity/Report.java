package com.example.unknowngserver.report.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
@SQLDelete(sql = "UPDATE report SET processed = true, processedAt = current_timestamp WHERE id = ? ")
@Where(clause = "processed = false")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "content_type")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime firstReportedAt;
    @Column(name="content_type", nullable=false, updatable=false, insertable=false)
    private String contentType;
    private Integer contentId;

    @Builder.Default
    private boolean processed = Boolean.FALSE;
    private LocalDateTime processedAt;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report", cascade = CascadeType.REMOVE)
    private List<ReportRecord> reportDetails = new ArrayList<>();

    public void addReportDetail(ReportRecord reportRecord) {
        reportRecord.addReport(this);
        this.reportDetails.add(reportRecord);
    }
}
