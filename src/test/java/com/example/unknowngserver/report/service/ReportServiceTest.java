package com.example.unknowngserver.report.service;

import com.example.unknowngserver.common.dto.PageNumber;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.exception.ReportException;
import com.example.unknowngserver.report.dto.ReportDto;
import com.example.unknowngserver.report.dto.ReportRecordDto;
import com.example.unknowngserver.report.entity.Report;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.entity.ReportComment;
import com.example.unknowngserver.report.entity.ReportRecord;
import com.example.unknowngserver.report.repository.ReportRecordRepository;
import com.example.unknowngserver.report.repository.ReportRepository;
import com.example.unknowngserver.report.type.ReportType;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    private ReportService reportService;

    ReportArticle reportArticle;
    ReportComment reportComment;
    ReportRecord reportRecord1, reportRecord2, reportRecord3;
    List<Report> reportList;
    List<ReportRecord> reportRecordList;
    @BeforeEach
    public void beforeEach() {

        reportList = new ArrayList<>();
        reportRecordList = new ArrayList<>();

        reportArticle = ReportArticle.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("ARTICLE")
                .reportedCount(1)
                .build();
        reportComment = ReportComment.builder()
                .id(2L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("COMMENT")
                .reportedCount(2)
                .build();

        reportList.add(reportArticle);
        reportList.add(reportComment);

        reportRecord1 = ReportRecord.builder()
                .id(1L)
                .report(reportArticle)
                .reportType(ReportType.INSULT)
                .memo("test report record")
                .reportedDt(LocalDateTime.now())
                .build();
        reportRecord2 = ReportRecord.builder()
                .id(2L)
                .report(reportArticle)
                .reportType(ReportType.INSULT)
                .memo("test report record")
                .reportedDt(LocalDateTime.now())
                .build();
        reportRecord3 = ReportRecord.builder()
                .id(3L)
                .report(reportArticle)
                .reportType(ReportType.INSULT)
                .memo("test report record")
                .reportedDt(LocalDateTime.now())
                .build();

        reportRecordList.add(reportRecord1);
        reportRecordList.add(reportRecord2);
        reportRecordList.add(reportRecord3);

    }

    @Test
    @DisplayName("신고 목록 조회 요청시, 신고 목록을 가져온다.")
    void getReports() {

        //given
        Page<Report> reportPageList = new PageImpl<>(reportList);

        given(reportRepository.findAll((Pageable) any()))
                .willReturn(reportPageList);

        //when
        List<ReportDto> reportDtoList = reportService.getReports(new PageNumber(1));

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
    @DisplayName("신고 기록 목록 조회 요청시, 신고 기록 목록을 가져온다.")
    void getReportRecords() {

        //given
        Page<ReportRecord> reportRecordPageList = new PageImpl<>(reportRecordList);

        given(reportRepository.findById(1L))
                .willReturn(Optional.of(reportArticle));
        given(reportRecordRepository.findAllByReport(any(), any()))
                .willReturn(reportRecordPageList);

        ArgumentCaptor<Report> reportArgumentCaptor = ArgumentCaptor.forClass(Report.class);

        //when
        List<ReportRecordDto> reportRecordDtoList = reportService.getReportRecords(1L, new PageNumber(1));

        //then
        verify(reportRepository).findById(1L);
        verify(reportRecordRepository).findAllByReport(reportArgumentCaptor.capture(), any());
        assertEquals(reportArticle, reportArgumentCaptor.getValue());
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
    @DisplayName("특정 신고의 신고 기록 목록 조회 요청시, 헤딩신고내역이 없을 경우 예외를 발생시킨다.")
    void getReportRecordsFail_ReportNotFound() {

        //given
        given(reportRepository.findById(1L))
                .willReturn(Optional.empty());

        ArgumentCaptor<Report> reportArgumentCaptor = ArgumentCaptor.forClass(Report.class);

        //when
        ReportException reportException = assertThrows(ReportException.class,
                () -> reportService.getReportRecords(1L, new PageNumber(1)));

        //then
        verify(reportRepository).findById(1L);
        verify(reportRecordRepository, never()).findAllByReport(any(), any());
        assertThat(ErrorCode.REPORT_NOT_FOUND).isEqualTo(reportException.getErrorCode());

    }

}
