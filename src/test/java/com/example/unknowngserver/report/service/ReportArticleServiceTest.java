package com.example.unknowngserver.report.service;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.entity.ReportRecord;
import com.example.unknowngserver.report.repository.ReportArticleRepository;
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
class ReportArticleServiceTest {

    @Mock
    private ReportArticleRepository reportArticleRepository;
    @Mock
    private ReportRecordRepository reportRecordRepository;
    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ReportArticleService reportArticleService;

    @Test
    @DisplayName("신고 상세 내용 조회 API 서비스 테스트 성공 - 게시글 신고")
    void getReportArticleDetail() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportArticle reportArticle = ReportArticle.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("ARTICLE")
                .reportedCount(1)
                .article(article)
                .build();

        //when
        ReportDetailDto reportDetailDto = reportArticleService.getReportArticleDetail(reportArticle);

        //then
        assertEquals(1L, reportDetailDto.getReportId());
        assertEquals("ARTICLE", reportDetailDto.getReportedContentType());
        assertEquals(1, reportDetailDto.getReportedCount());
        assertEquals(1L, reportDetailDto.getTargetId());
        assertEquals("테스트용", reportDetailDto.getTargetContent());

    }

    @Test
    @DisplayName("신고 등록 API 서비스 테스트 성공 - 게시글 신고 (기존 게시글에 신고내역이 없을때)")
    void createReportArticle() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportArticle reportArticle = ReportArticle.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("ARTICLE")
                .reportedCount(1)
                .article(article)
                .build();

        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(reportArticleRepository.findByArticle(article)).willReturn(Optional.empty());
        given(reportArticleRepository.save(any())).willReturn(reportArticle);

        ArgumentCaptor<ReportRecord> reportRecordArgumentCaptor = ArgumentCaptor.forClass(ReportRecord.class);

        // When
        boolean result = reportArticleService.createReportArticle(new SubmitReportRequest("ARTICLE", 1L, "INSULT", "test memo"));

        // Then
        verify(reportArticleRepository).findByArticle(any());
        verify(reportArticleRepository).save(any());
        verify(reportRecordRepository).save(reportRecordArgumentCaptor.capture());
        assertTrue(result);
        assertEquals(reportArticle, reportRecordArgumentCaptor.getValue().getReport());

    }

    @Test
    @DisplayName("신고 등록 API 서비스 테스트 성공 - 게시글 신고 (기존 게시글에 신고내역이 있을때)")
    void createReportArticle_ArticleAlreadyReportedBefore() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportArticle reportArticle = ReportArticle.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("ARTICLE")
                .reportedCount(1)
                .article(article)
                .build();

        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(reportArticleRepository.findByArticle(article)).willReturn(Optional.of(reportArticle));

        ArgumentCaptor<ReportRecord> reportRecordArgumentCaptor = ArgumentCaptor.forClass(ReportRecord.class);

        // When
        boolean result = reportArticleService.createReportArticle(new SubmitReportRequest("ARTICLE", 1L, "INSULT", "test memo"));

        // Then
        verify(reportArticleRepository).findByArticle(any());
        verify(reportArticleRepository, never()).save(any());
        verify(reportRecordRepository).save(reportRecordArgumentCaptor.capture());
        assertTrue(result);
        assertEquals(reportArticle, reportRecordArgumentCaptor.getValue().getReport());

    }

    @Test
    @DisplayName("신고 삭제 API 서비스 테스트 성공 - 게시글 신고")
    void deleteReportArticle() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportArticle reportArticle = ReportArticle.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("ARTICLE")
                .reportedCount(1)
                .article(article)
                .build();


        //when
        boolean result = reportArticleService.deleteReportArticle(reportArticle);

        //then
        verify(articleRepository).delete(article);
        verify(reportArticleRepository).delete(reportArticle);
        assertTrue(result);

    }
}