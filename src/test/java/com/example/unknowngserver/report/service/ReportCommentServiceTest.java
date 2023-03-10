package com.example.unknowngserver.report.service;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.comment.entity.Comment;
import com.example.unknowngserver.comment.repository.CommentRepository;
import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.entity.Report;
import com.example.unknowngserver.report.entity.ReportComment;
import com.example.unknowngserver.report.entity.ReportRecord;
import com.example.unknowngserver.report.repository.ReportCommentRepository;
import com.example.unknowngserver.report.repository.ReportRecordRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportCommentServiceTest {

    @Mock
    private ReportCommentRepository reportCommentRepository;
    @Mock
    private ReportRecordRepository reportRecordRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ReportCommentService reportCommentService;

    Comment comment;
    ReportComment report;

    @BeforeEach
    public void beforeEach() {
        comment = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        report = ReportComment.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("COMMENT")
                .reportedCount(1)
                .comment(comment)
                .build();
    }

    @Test
    @DisplayName("신고 상세 내용 조회 요청시, 신고 상세 내용을 보여준다.")
    void getReportCommentDetail() {

        //given
        //when
        ReportDetailDto reportDetailDto = reportCommentService.getReportDetail(report);

        //then
        assertEquals(1L, reportDetailDto.getReportId());
        assertEquals("COMMENT", reportDetailDto.getReportedContentType());
        assertEquals(1, reportDetailDto.getReportedCount());
        assertEquals(1L, reportDetailDto.getTargetId());
        assertEquals("comment test 1", reportDetailDto.getTargetContent());

    }

    @Test
    @DisplayName("신고 등록 요청시, 기존 신고 내용이 없을 경우 새 신고를 등록한다.")
    void createReportComment() {

        //given
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        given(reportCommentRepository.findByComment(comment)).willReturn(Optional.empty());
        given(reportCommentRepository.save(any())).willReturn(report);

        ArgumentCaptor<ReportRecord> reportRecordArgumentCaptor = ArgumentCaptor.forClass(ReportRecord.class);

        //when
        reportCommentService.createReport(new SubmitReportRequest("COMMENT", 1L, "INSULT", "test memo"));

        //then
        verify(reportCommentRepository).findByComment(any());
        verify(reportCommentRepository).save(any());
        verify(reportRecordRepository).save(reportRecordArgumentCaptor.capture());
        assertEquals(report, reportRecordArgumentCaptor.getValue().getReport());

    }

    @Test
    @DisplayName("신고 등록 요청시, 기존 신고 내용이 있을 경우 신고를 업데이트한다.")
    void createReportComment_CommentAlreadyReportedBefore() {

        //given
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
        given(reportCommentRepository.findByComment(comment)).willReturn(Optional.of(report));

        ArgumentCaptor<ReportRecord> reportRecordArgumentCaptor = ArgumentCaptor.forClass(ReportRecord.class);

        //when
        reportCommentService.createReport(new SubmitReportRequest("COMMENT", 1L, "INSULT", "test memo"));

        //then
        verify(reportCommentRepository).findByComment(any());
        verify(reportCommentRepository, never()).save(any());
        verify(reportRecordRepository).save(reportRecordArgumentCaptor.capture());
        assertEquals(report, reportRecordArgumentCaptor.getValue().getReport());

    }

    @Test
    @DisplayName("신고 삭제 요청시, 신고를 삭제한다.")
    void deleteReportComment() {

        //given
        //when
        reportCommentService.deleteReport(report);

        //then
        verify(commentRepository).delete(comment);
        verify(reportCommentRepository).delete(report);

    }
} 