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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @DisplayName("신고 상세 내용 조회 API 서비스 테스트 성공 - 댓글 신고")
    void getReportCommentDetail() {

        //given
        Comment comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportComment report = ReportComment.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("COMMENT")
                .reportedCount(1)
                .comment(comment1)
                .build();

        //when
        ReportDetailDto reportDetailDto = reportCommentService.getReportCommentDetail(report);

        //then
        assertEquals(1L, reportDetailDto.getReportId());
        assertEquals("COMMENT", reportDetailDto.getReportedContentType());
        assertEquals(1, reportDetailDto.getReportedCount());
        assertEquals(1L, reportDetailDto.getTargetId());
        assertEquals("comment test 1", reportDetailDto.getTargetContent());

    }

    @Test
    @DisplayName("신고 등록 API 서비스 테스트 성공 - 댓글 신고 (기존 댓글에 신고내역이 없을때)")
    void createReportComment() {

        //given
        Comment comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportComment report = ReportComment.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("COMMENT")
                .reportedCount(1)
                .comment(comment1)
                .build();

        given(commentRepository.findById(1L)).willReturn(Optional.of(comment1));
        given(reportCommentRepository.findByComment(comment1)).willReturn(Optional.empty());
        given(reportCommentRepository.save(any())).willReturn(report);

        ArgumentCaptor<ReportRecord> reportRecordArgumentCaptor = ArgumentCaptor.forClass(ReportRecord.class);

        //when
        boolean result = reportCommentService.createReportComment(new SubmitReportRequest("COMMET", 1L, "INSULT", "test memo"));

        //then
        verify(reportCommentRepository).findByComment(any());
        verify(reportCommentRepository).save(any());
        verify(reportRecordRepository).save(reportRecordArgumentCaptor.capture());
        assertTrue(result);
        assertEquals(report, reportRecordArgumentCaptor.getValue().getReport());

    }

    @Test
    @DisplayName("신고 등록 API 서비스 테스트 성공 - 댓글 신고 (기존 댓글에 신고내역이 없을때)")
    void createReportComment_CommentAlreadyReportedBefore() {

        //given
        Comment comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportComment report = ReportComment.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("COMMENT")
                .reportedCount(1)
                .comment(comment1)
                .build();

        given(commentRepository.findById(1L)).willReturn(Optional.of(comment1));
        given(reportCommentRepository.findByComment(comment1)).willReturn(Optional.of(report));

        ArgumentCaptor<ReportRecord> reportRecordArgumentCaptor = ArgumentCaptor.forClass(ReportRecord.class);

        //when
        boolean result = reportCommentService.createReportComment(new SubmitReportRequest("COMMET", 1L, "INSULT", "test memo"));

        //then
        verify(reportCommentRepository).findByComment(any());
        verify(reportCommentRepository, never()).save(any());
        verify(reportRecordRepository).save(reportRecordArgumentCaptor.capture());
        assertTrue(result);
        assertEquals(report, reportRecordArgumentCaptor.getValue().getReport());

    }

    @Test
    @DisplayName("신고 삭제 API 서비스 테스트 성공 - 댓글 신고")
    void deleteReportComment() {

        //given
        Comment comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportComment report = ReportComment.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("COMMENT")
                .reportedCount(1)
                .comment(comment1)
                .build();

        //when
        boolean result = reportCommentService.deleteReportComment(report);

        //then
        verify(commentRepository).delete(comment1);
        verify(reportCommentRepository).delete(report);
        assertTrue(result);

    }
}