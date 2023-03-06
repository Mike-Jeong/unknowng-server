package com.example.unknowngserver.report.service;

import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.entity.Report;

public interface ReportContentService {

    void createReport(SubmitReportRequest submitReportRequest);
    void deleteReport(Report report);
    ReportDetailDto getReportDetail(Report report);

}
