package com.example.unknowngserver.report.service;

import com.example.unknowngserver.comment.entity.Comment;
import com.example.unknowngserver.comment.repository.CommentRepository;
import com.example.unknowngserver.exception.CommentException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.entity.Report;
import com.example.unknowngserver.report.entity.ReportComment;
import com.example.unknowngserver.report.entity.ReportRecord;
import com.example.unknowngserver.report.repository.ReportCommentRepository;
import com.example.unknowngserver.report.repository.ReportRecordRepository;
import com.example.unknowngserver.report.type.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportCommentService {

    private final ReportCommentRepository reportCommentRepository;
    private final ReportRecordRepository reportRecordRepository;
    private final CommentRepository commentRepository;

    public ReportDetailDto getReportCommentDetail(Report report) {

        ReportComment reportComment = (ReportComment) report;

        return ReportDetailDto.builder()
                .reportId(reportComment.getId())
                .reportedContentType(reportComment.getContentType())
                .targetId(reportComment.getComment().getId())
                .targetContent(reportComment.getComment().getContent())
                .reportedCount(reportComment.getReportedCount())
                .firstReportedAt(reportComment.getFirstReportedAt())
                .build();
    }

    @Transactional
    public boolean createReportComment(SubmitReportRequest submitReportRequest) {

        Comment comment = findComment(submitReportRequest.getContentId());
        LocalDateTime currentTimeStamp = LocalDateTime.now();

        ReportComment reportComment = reportCommentRepository.findByComment(comment)
                .orElseGet(() -> reportCommentRepository.save(ReportComment.builder()
                        .firstReportedAt(currentTimeStamp)
                        .comment(comment)
                        .build()));

        reportRecordRepository.save(ReportRecord.builder()
                .report(reportComment)
                .reportType(ReportType.valueOf(submitReportRequest.getReportType()))
                .memo(submitReportRequest.getMemo())
                .reportedDt(currentTimeStamp)
                .build());

        reportComment.updateReportedCount(reportRecordRepository.countReportRecordByReport(reportComment));

        return true;
    }

    @Transactional
    public boolean deleteReportComment(ReportComment report) {

        commentRepository.delete(report.getComment());
        reportCommentRepository.delete(report);

        return true;
    }

    private Comment findComment(Long commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

}
