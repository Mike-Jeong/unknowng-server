package com.example.unknowngserver.report.service;

import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.exception.ReportException;
import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.ReportDto;
import com.example.unknowngserver.report.dto.ReportRecordDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.entity.Report;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.entity.ReportComment;
import com.example.unknowngserver.report.entity.ReportRecord;
import com.example.unknowngserver.report.repository.ReportRecordRepository;
import com.example.unknowngserver.report.repository.ReportRepository;
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
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportRecordRepository reportRecordRepository;
    private final ReportArticleService reportArticleService;
    private final ReportCommentService reportCommentService;

    @Transactional(readOnly = true)
    public List<ReportDto> getReports(Integer page) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        Page<Report> reportPageList = reportRepository.findAll(pageRequest);

        return reportPageList.stream()
                .map(ReportDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReportRecordDto> getReportRecords(Long reportId, Integer page) {

        Report report = findReport(reportId);

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("reportedDt").descending());

        Page<ReportRecord> reportRecordPageList = reportRecordRepository.findAllByReport(report, pageRequest);

        return reportRecordPageList.stream()
                .map(ReportRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

    public boolean createReport(SubmitReportRequest submitReportRequest) {

        if (submitReportRequest.getContentType().equals("ARTICLE")) {
            return reportArticleService.createReportArticle(submitReportRequest);
        }

        if (submitReportRequest.getContentType().equals("COMMENT")) {
            return reportCommentService.createReportComment(submitReportRequest);
        }

        throw new ReportException(ErrorCode.REPORT_DETAIL_CONTENT_TYPE_NOT_SUPPORTED);
    }

    public boolean deleteReport(Long reportId) {

        Report report = findReport(reportId);

        if (report.getContentType().equals("ARTICLE")) {
            return reportArticleService.deleteReportArticle((ReportArticle) report);
        }

        if (report.getContentType().equals("COMMENT")) {
            return reportCommentService.deleteReportComment((ReportComment) report);
        }

        throw new ReportException(ErrorCode.REPORT_DETAIL_CONTENT_TYPE_NOT_SUPPORTED);
    }

    public ReportDetailDto getReportDetail(Long reportId) {

        Report report = findReport(reportId);

        if (report.getContentType().equals("ARTICLE")) {
            return reportArticleService.getReportArticleDetail((ReportArticle) report);
        }

        if (report.getContentType().equals("COMMENT")) {
            return reportCommentService.getReportCommentDetail((ReportComment) report);
        }

        throw new ReportException(ErrorCode.REPORT_DETAIL_CONTENT_TYPE_NOT_SUPPORTED);
    }

    public Report findReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ErrorCode.REPORT_NOT_FOUND));
    }
}
