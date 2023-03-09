package com.example.unknowngserver.report.service;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.entity.ReportRecord;
import com.example.unknowngserver.report.repository.ReportArticleRepository;
import com.example.unknowngserver.report.repository.ReportRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    Article article;
    ReportArticle reportArticle;

    @BeforeEach
    public void beforeEach() {
        article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        reportArticle = ReportArticle.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .contentType("ARTICLE")
                .reportedCount(1)
                .article(article)
                .build();
    }

    @Test
    @DisplayName("신고 상세 내용 조회 요청시, 신고 상세 내용을 보여준다.")
    void getReportArticleDetail() {

        //given
        //when
        ReportDetailDto reportDetailDto = reportArticleService.getReportDetail(reportArticle);

        //then
        assertThat(1L).isEqualTo(reportDetailDto.getReportId());
        assertThat("ARTICLE").isEqualTo(reportDetailDto.getReportedContentType());
        assertThat(1).isEqualTo(reportDetailDto.getReportedCount());
        assertThat(1L).isEqualTo(reportDetailDto.getTargetId());
        assertThat("테스트용").isEqualTo(reportDetailDto.getTargetContent());

    }

    @Test
    @DisplayName("신고 등록 요청시, 기존 신고 내용이 없을 경우 새 신고를 등록한다.")
    void createReportArticle() {

        //given
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(reportArticleRepository.findByArticle(article)).willReturn(Optional.empty());
        given(reportArticleRepository.save(any())).willReturn(reportArticle);

        ArgumentCaptor<ReportRecord> reportRecordArgumentCaptor = ArgumentCaptor.forClass(ReportRecord.class);

        // When
        reportArticleService.createReport(new SubmitReportRequest("ARTICLE", 1L, "INSULT", "test memo"));

        // Then
        verify(reportArticleRepository).findByArticle(any());
        verify(reportArticleRepository).save(any());
        verify(reportRecordRepository).save(reportRecordArgumentCaptor.capture());
        assertThat(reportArticle).isEqualTo(reportRecordArgumentCaptor.getValue().getReport());

    }

    @Test
    @DisplayName("신고 등록 요청시, 기존 신고 내용이 있을 경우 신고를 업데이트한다.")
    void createReportArticle_ArticleAlreadyReportedBefore() {

        //given
        given(articleRepository.findById(1L)).willReturn(Optional.of(article));
        given(reportArticleRepository.findByArticle(article)).willReturn(Optional.of(reportArticle));

        ArgumentCaptor<ReportRecord> reportRecordArgumentCaptor = ArgumentCaptor.forClass(ReportRecord.class);

        // When
        reportArticleService.createReport(new SubmitReportRequest("ARTICLE", 1L, "INSULT", "test memo"));

        // Then 
        verify(reportArticleRepository).findByArticle(any());
        verify(reportArticleRepository, never()).save(any());
        verify(reportRecordRepository).save(reportRecordArgumentCaptor.capture());
        assertThat(reportArticle).isEqualTo(reportRecordArgumentCaptor.getValue().getReport());

    }

    @Test
    @DisplayName("신고 삭제 요청시, 신고를 삭제한다.")
    void deleteReportArticle() {

        //given
        //when
        reportArticleService.deleteReport(reportArticle);

        //then 
        verify(articleRepository).delete(article);
        verify(reportArticleRepository).delete(reportArticle);

    }
} 