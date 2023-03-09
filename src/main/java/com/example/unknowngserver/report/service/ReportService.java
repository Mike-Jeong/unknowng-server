package com.example.unknowngserver.report.service;

import com.example.unknowngserver.common.dto.PageNumber;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.exception.ReportException;
import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.ReportDto;
import com.example.unknowngserver.report.dto.ReportRecordDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.entity.Report;
import com.example.unknowngserver.report.entity.ReportRecord;
import com.example.unknowngserver.report.repository.ReportRecordRepository;
import com.example.unknowngserver.report.repository.ReportRepository;
import com.example.unknowngserver.report.type.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private static final int PAGE_SIZE = 10;

    private final ReportRepository reportRepository;
    private final ReportRecordRepository reportRecordRepository;

    public List<ReportDto> getReports(PageNumber page) {

        PageRequest pageRequest = PageRequest.of(page.getPage(), PAGE_SIZE, Sort.by("reportedCount").descending());

        Page<Report> reportPageList = reportRepository.findAll(pageRequest);

        return reportPageList.stream()
                .map(ReportDto::fromReport)
                .collect(Collectors.toList());
    }

    public List<ReportRecordDto> getReportRecords(Long reportId, PageNumber page) {

        Report report = findReport(reportId);

        PageRequest pageRequest = PageRequest.of(page.getPage(), PAGE_SIZE, Sort.by("reportedDt").descending());

        Page<ReportRecord> reportRecordPageList = reportRecordRepository.findAllByReport(report, pageRequest);

        return reportRecordPageList.stream()
                .map(ReportRecordDto::fromReportRecord)
                .collect(Collectors.toList());
    }

    public ReportDetailDto getReportDetail(Long reportId) {

        Report report = findReport(reportId);

        ReportContentService reportContentService = ContentType.getReportContentService(report.getContentType());

        return reportContentService.getReportDetail(report);

    }

    public void createReport(SubmitReportRequest submitReportRequest) {

        ReportContentService reportContentService = ContentType.getReportContentService(submitReportRequest.getContentType());

        reportContentService.createReport(submitReportRequest);

    }

    @Transactional
    public void deleteReport(Long reportId) {

        Report report = findReport(reportId);

        ReportContentService reportContentService = ContentType.getReportContentService(report.getContentType());

        reportContentService.deleteReport(report);

    }

    private Report findReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ErrorCode.REPORT_NOT_FOUND));
    }
}
