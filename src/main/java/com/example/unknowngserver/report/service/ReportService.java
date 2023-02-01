package com.example.unknowngserver.report.service;

import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.comment.repository.CommentRepository;
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
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<ReportDto> getReports(Integer page){

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<Report> reportPageList = reportRepository.findAll(pageRequest);

        return reportPageList.stream()
                .map(ReportDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReportRecordDto> getReportRecords(Long reportId, Integer page) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ErrorCode.REPORT_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<ReportRecord> reportRecordPageList = reportRecordRepository.findAllByReport(report, pageRequest);

        return reportRecordPageList.stream()
                .map(ReportRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReportDetailDto getReportDetail(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ErrorCode.REPORT_NOT_FOUND));

        return report.getContentType().equals("ARTICLE") ? ReportDetailDto.fromReportArticleEntity((ReportArticle) report)
                : ReportDetailDto.fromReportCommentEntity((ReportComment) report);
    }

    @Transactional
    public boolean deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ErrorCode.REPORT_NOT_FOUND));

        reportRepository.delete(report);
        return true;
    }

    /*@Transactional
    public boolean createReport(SubmitReportRequest submitReportRequest) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ErrorCode.REPORT_NOT_FOUND));

        reportRepository.delete(report);
        return true;
    }

    public Report findReportFromArticle (Long articleId) {
        Report report = articleRepository.findById(articleId).get().getReportArticle();
    }

    public Report findReportFromComment (Long articleId) {

    }*/
}
