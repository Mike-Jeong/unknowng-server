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
import com.example.unknowngserver.report.type.ReportType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private ReportRecordRepository reportRecordRepository;
    @Mock
    private ReportArticleService reportArticleService;
    @Mock
    private ReportCommentService reportCommentService;

    @InjectMocks
    private ReportService reportService;


    @Test
    @DisplayName("신고 조회 API 서비스 테스트 성공")
    void getReports() {

        //given
        Report reportA = Report.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("ARTICLE")
                .reportedCount(1)
                .build();
        Report reportB = Report.builder()
                .id(2L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("COMMENT")
                .reportedCount(2)
                .build();

        List<Report> reportList = new ArrayList<>();
        reportList.add(reportA);
        reportList.add(reportB);

        Page<Report> reportPageList = new PageImpl<>(reportList);

        given(reportRepository.findAll((Pageable) any()))
                .willReturn(reportPageList);

        //when
        List<ReportDto> reportDtoList = reportService.getReports(1);

        //then
        verify(reportRepository).findAll((Pageable) any());
        assertEquals(2, reportDtoList.size());
        assertEquals(1L, reportDtoList.get(0).getReportId());
        assertEquals("ARTICLE", reportDtoList.get(0).getReportedContentType());
        assertEquals(1, reportDtoList.get(0).getReportedCount());
        assertEquals(2L, reportDtoList.get(1).getReportId());
        assertEquals("COMMENT", reportDtoList.get(1).getReportedContentType());
        assertEquals(2, reportDtoList.get(1).getReportedCount());

    }

    @Test
    @DisplayName("신고 기록 조회 API 서비스 테스트 성공")
    void getReportRecords() {

        //given
        Report reportA = Report.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("ARTICLE")
                .reportedCount(1)
                .build();

        ReportRecord reportRecord1 = ReportRecord.builder()
                .id(1L)
                .report(reportA)
                .reportType(ReportType.INSULT)
                .memo("test report record")
                .reportedDt(LocalDateTime.now())
                .build();
        ReportRecord reportRecord2 = ReportRecord.builder()
                .id(2L)
                .report(reportA)
                .reportType(ReportType.INSULT)
                .memo("test report record")
                .reportedDt(LocalDateTime.now())
                .build();
        ReportRecord reportRecord3 = ReportRecord.builder()
                .id(3L)
                .report(reportA)
                .reportType(ReportType.INSULT)
                .memo("test report record")
                .reportedDt(LocalDateTime.now())
                .build();

        List<ReportRecord> reportRecordList = new ArrayList<>();
        reportRecordList.add(reportRecord1);
        reportRecordList.add(reportRecord2);
        reportRecordList.add(reportRecord3);

        Page<ReportRecord> reportRecordPageList = new PageImpl<>(reportRecordList);

        given(reportRepository.findById(1L))
                .willReturn(Optional.of(reportA));
        given(reportRecordRepository.findAllByReport(any(), any()))
                .willReturn(reportRecordPageList);

        ArgumentCaptor<Report> reportArgumentCaptor = ArgumentCaptor.forClass(Report.class);

        //when
        List<ReportRecordDto> reportRecordDtoList = reportService.getReportRecords(1L, 1);

        //then
        verify(reportRepository).findById(1L);
        verify(reportRecordRepository).findAllByReport(reportArgumentCaptor.capture(), any());
        assertEquals(reportA, reportArgumentCaptor.getValue());
        assertEquals(3, reportRecordDtoList.size());
        assertEquals(1L, reportRecordDtoList.get(0).getId());
        assertEquals("test report record", reportRecordDtoList.get(0).getMemo());
        assertEquals(ReportType.INSULT, reportRecordDtoList.get(0).getReportType());
        assertEquals(2L, reportRecordDtoList.get(1).getId());
        assertEquals("test report record", reportRecordDtoList.get(1).getMemo());
        assertEquals(ReportType.INSULT, reportRecordDtoList.get(1).getReportType());
        assertEquals(3L, reportRecordDtoList.get(2).getId());
        assertEquals("test report record", reportRecordDtoList.get(2).getMemo());
        assertEquals(ReportType.INSULT, reportRecordDtoList.get(2).getReportType());

    }

    @Test
    @DisplayName("신고 기록 조회 API 서비스 테스트 실패 - 해당 신고 내역을 찾을 수 없음")
    void getReportRecordsFail_ReportNotFound() {

        //given
        given(reportRepository.findById(1L))
                .willReturn(Optional.empty());

        //when
        ReportException reportException = assertThrows(ReportException.class,
                () -> reportService.getReportRecords(1L, 1));

        //then
        verify(reportRepository).findById(1L);
        assertEquals(ErrorCode.REPORT_NOT_FOUND, reportException.getErrorCode());

    }

    @Test
    @DisplayName("신고 등록 API 서비스 테스트 성공 - 게시글 신고")
    void createReportArticle() {

        //given
        given(reportArticleService.createReportArticle(any()))
                .willReturn(true);

        //when
        boolean result = reportService.createReport(new SubmitReportRequest("ARTICLE", 1L, "INSULT", "test memo"));

        //then
        verify(reportArticleService).createReportArticle(any());
        verify(reportCommentService, never()).createReportComment(any());
        assertTrue(result);

    }

    @Test
    @DisplayName("신고 등록 API 서비스 테스트 성공 - 댓글 신고")
    void createReportComment() {

        //given
        given(reportCommentService.createReportComment(any()))
                .willReturn(true);

        //when
        boolean result = reportService.createReport(new SubmitReportRequest("COMMENT", 1L, "INSULT", "test memo"));

        //then
        verify(reportArticleService, never()).createReportArticle(any());
        verify(reportCommentService).createReportComment(any());
        assertTrue(result);

    }

    @Test
    @DisplayName("신고 등록 API 서비스 테스트 실패 - 지원하지 않는 컨텐츠 타입 신고")
    void createReportFail_NotSupportedContentType() {

        //given
        //when
        ReportException reportException = assertThrows(ReportException.class,
                () -> reportService.createReport(new SubmitReportRequest("OTHER", 1L, "INSULT", "test memo")));

        //then
        verify(reportArticleService, never()).createReportArticle(any());
        verify(reportCommentService, never()).createReportComment(any());
        assertEquals(ErrorCode.REPORT_DETAIL_CONTENT_TYPE_NOT_SUPPORTED, reportException.getErrorCode());

    }

    @Test
    @DisplayName("신고 삭제 API 서비스 테스트 성공 - 게시글 신고")
    void deleteReportArticle() {

        //given
        ReportArticle reportArticle = ReportArticle.builder()
                .id(1L)
                .contentType("ARTICLE")
                .reportedCount(1)
                .build();

        given(reportRepository.findById(1L))
                .willReturn(Optional.of(reportArticle));
        given(reportArticleService.deleteReportArticle(any()))
                .willReturn(true);

        ArgumentCaptor<ReportArticle> reportArticleArgumentCaptor = ArgumentCaptor.forClass(ReportArticle.class);

        //when
        boolean result = reportService.deleteReport(1L);

        //then
        verify(reportArticleService).deleteReportArticle(reportArticleArgumentCaptor.capture());
        verify(reportCommentService, never()).deleteReportComment(any());
        assertTrue(result);
        assertEquals(reportArticle, reportArticleArgumentCaptor.getValue());

    }

    @Test
    @DisplayName("신고 삭제 API 서비스 테스트 성공 - 댓글 신고")
    void deleteReportComment() {

        //given
        ReportComment reportComment = ReportComment.builder()
                .id(1L)
                .contentType("COMMENT")
                .reportedCount(1)
                .build();

        given(reportRepository.findById(1L))
                .willReturn(Optional.of(reportComment));
        given(reportCommentService.deleteReportComment(any()))
                .willReturn(true);

        ArgumentCaptor<ReportComment> reportCommentArgumentCaptor = ArgumentCaptor.forClass(ReportComment.class);

        //when
        boolean result = reportService.deleteReport(1L);

        //then
        verify(reportArticleService, never()).deleteReportArticle(any());
        verify(reportCommentService).deleteReportComment(reportCommentArgumentCaptor.capture());
        assertTrue(result);
        assertEquals(reportComment, reportCommentArgumentCaptor.getValue());

    }

    @Test
    @DisplayName("신고 삭제 API 서비스 테스트 실패 - 해당 신고 내역을 찾을 수 없음")
    void deleteReportFail_ReportNotFound() {

        //given
        given(reportRepository.findById(1L))
                .willReturn(Optional.empty());

        //when
        ReportException reportException = assertThrows(ReportException.class,
                () -> reportService.deleteReport(1L));

        //then
        verify(reportRepository).findById(1L);
        assertEquals(ErrorCode.REPORT_NOT_FOUND, reportException.getErrorCode());

    }

    @Test
    @DisplayName("신고 삭제 API 서비스 테스트 실패 - 지원하지 않는 컨텐츠 타입 신고")
    void deleteReportFail_NotSupportedContentType() {

        //given
        Report reportA = Report.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("OTHER")
                .reportedCount(1)
                .build();

        given(reportRepository.findById(1L))
                .willReturn(Optional.of(reportA));

        //when
        ReportException reportException = assertThrows(ReportException.class,
                () -> reportService.deleteReport(1L));

        //then
        verify(reportRepository).findById(1L);
        assertEquals(ErrorCode.REPORT_DETAIL_CONTENT_TYPE_NOT_SUPPORTED, reportException.getErrorCode());

    }

    @Test
    @DisplayName("신고 상세 내용 조회 API 서비스 테스트 성공 - 게시글 신고")
    void getReportDetailArticle() {

        //given
        ReportArticle reportArticle = ReportArticle.builder()
                .id(1L)
                .contentType("ARTICLE")
                .reportedCount(1)
                .build();

        ReportDetailDto reportDetailDto = ReportDetailDto.builder()
                .reportId(1L)
                .reportedContentType("ARTICLE")
                .targetId(1L)
                .targetContent("test content")
                .reportedCount(1)
                .build();

        given(reportRepository.findById(1L))
                .willReturn(Optional.of(reportArticle));
        given(reportArticleService.getReportArticleDetail(any()))
                .willReturn(reportDetailDto);

        ArgumentCaptor<ReportArticle> reportArticleArgumentCaptor = ArgumentCaptor.forClass(ReportArticle.class);

        //when
        ReportDetailDto result = reportService.getReportDetail(1L);

        //then
        verify(reportArticleService).getReportArticleDetail(reportArticleArgumentCaptor.capture());
        verify(reportCommentService, never()).getReportCommentDetail(any());
        assertEquals(reportDetailDto, result);
        assertEquals(reportArticle, reportArticleArgumentCaptor.getValue());

    }

    @Test
    @DisplayName("신고 상세 내용 조회 API 서비스 테스트 성공 - 댓글 신고")
    void getReportDetailComment() {

        //given
        ReportComment reportComment = ReportComment.builder()
                .id(1L)
                .contentType("COMMENT")
                .reportedCount(1)
                .build();

        ReportDetailDto reportDetailDto = ReportDetailDto.builder()
                .reportId(1L)
                .reportedContentType("COMMENT")
                .targetId(1L)
                .targetContent("test content")
                .reportedCount(1)
                .build();

        given(reportRepository.findById(1L))
                .willReturn(Optional.of(reportComment));
        given(reportCommentService.getReportCommentDetail(any()))
                .willReturn(reportDetailDto);

        ArgumentCaptor<ReportComment> reportCommentArgumentCaptor = ArgumentCaptor.forClass(ReportComment.class);

        //when
        ReportDetailDto result = reportService.getReportDetail(1L);

        //then
        verify(reportArticleService, never()).getReportArticleDetail(any());
        verify(reportCommentService).getReportCommentDetail(reportCommentArgumentCaptor.capture());
        assertEquals(reportDetailDto, result);
        assertEquals(reportComment, reportCommentArgumentCaptor.getValue());
    }

    @Test
    @DisplayName("신고 상세 내용 조회 API 서비스 테스트 실패 - 해당 신고 내역을 찾을 수 없음")
    void getReportDetailFail_ReportNotFound() {

        //given
        given(reportRepository.findById(1L))
                .willReturn(Optional.empty());

        //when
        ReportException reportException = assertThrows(ReportException.class,
                () -> reportService.getReportDetail(1L));

        //then
        verify(reportRepository).findById(1L);
        assertEquals(ErrorCode.REPORT_NOT_FOUND, reportException.getErrorCode());

    }

    @Test
    @DisplayName("신고 상세 내용 조회 API 서비스 테스트 실패 - 지원하지 않는 컨텐츠 타입 신고")
    void getReportDetailFail_NotSupportedContentType() {

        //given
        Report reportA = Report.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("OTHER")
                .reportedCount(1)
                .build();

        given(reportRepository.findById(1L))
                .willReturn(Optional.of(reportA));

        //when
        ReportException reportException = assertThrows(ReportException.class,
                () -> reportService.getReportDetail(1L));

        //then
        verify(reportRepository).findById(1L);
        assertEquals(ErrorCode.REPORT_DETAIL_CONTENT_TYPE_NOT_SUPPORTED, reportException.getErrorCode());

    }

}