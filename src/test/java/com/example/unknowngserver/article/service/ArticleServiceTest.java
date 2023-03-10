package com.example.unknowngserver.article.service;

import com.example.unknowngserver.article.dto.ArticleDto;
import com.example.unknowngserver.article.dto.DeleteArticleRequest;
import com.example.unknowngserver.article.dto.SubmitArticleRequest;
import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.common.dto.Keyword;
import com.example.unknowngserver.common.dto.PageNumber;
import com.example.unknowngserver.exception.ArticleException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.repository.ReportArticleRepository;
import com.example.unknowngserver.util.PasswordUtil;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ReportArticleRepository reportArticleRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private ArticleService articleService;

    Article article;
    ArticleDto targetArticleDto;
    LocalDateTime currentTime = LocalDateTime.now();

    @BeforeEach
    public void beforeEach() {

        article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(currentTime)
                .build();

        targetArticleDto = ArticleDto.fromArticle(article);
    }

    Page<Article> initArticlePageList() {

        Article article2 = Article.builder()
                .id(2L)
                .title("test2")
                .content("테스트용2")
                .author("test2")
                .password("1234")
                .registeredAt(currentTime)
                .build();

        List<Article> articleList = new ArrayList<>();

        articleList.add(article);
        articleList.add(article2);

        return new PageImpl<>(articleList);
    }

    @Test
    @DisplayName("게시글 조회 요청시, 게시글을 가져온다.")
    void getArticle() {

        //given
        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));

        //when
        ArticleDto articleDto = articleService.getArticle(1L);

        //then
        verify(articleRepository).findById(1L);
        assertThat(targetArticleDto.getId()).isEqualTo(articleDto.getId());
        assertThat(targetArticleDto.getAuthor()).isEqualTo(articleDto.getAuthor());
        assertThat(targetArticleDto.getContent()).isEqualTo(articleDto.getContent());
        assertThat(targetArticleDto.getTitle()).isEqualTo(articleDto.getTitle());

    }

    @Test
    @DisplayName("게시글 조회 요청시, 게시글이 존재하지 않을 경우 예외를 발생시킨다.")
    void getArticleFail() {

        //given
        given(articleRepository.findById(1L))
                .willReturn(Optional.empty());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> articleService.getArticle(1L));

        //then
        verify(articleRepository).findById(1L);
        assertThat(ErrorCode.ARTICLE_NOT_FOUND).isEqualTo(articleException.getErrorCode());

    }

    @Test
    @DisplayName("게시글 목록 조회 요청시, 게시글 목록을 가져온다.")
    void getArticles() {

        //given
        Page<Article> articlePageList = initArticlePageList();

        given(articleRepository.findAll((Pageable) any()))
                .willReturn(articlePageList);

        //when
        List<ArticleDto> articleDtoList = articleService.getArticles(new PageNumber(1), new Keyword(null));

        //then

        verify(articleRepository).findAll((Pageable) any());
        assertThat(2).isEqualTo(articleDtoList.size());
        assertThat(targetArticleDto.getId()).isEqualTo(articleDtoList.get(0).getId());
        assertThat(targetArticleDto.getAuthor()).isEqualTo(articleDtoList.get(0).getAuthor());
        assertThat(targetArticleDto.getContent()).isEqualTo(articleDtoList.get(0).getContent());
        assertThat(targetArticleDto.getTitle()).isEqualTo(articleDtoList.get(0).getTitle());
        assertThat(2L).isEqualTo(articleDtoList.get(1).getId());

    }

    @Test
    @DisplayName("검색어와 함께 게시글 목록 조회 요청시, 검색어와 관련있는 게시글 목록을 가져온다.")
    void getArticlesWithKeyword() {

        //given
        Page<Article> articlePageList = initArticlePageList();

        given(articleRepository.findByTitleContains(any(), anyString()))
                .willReturn(articlePageList);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //when
        List<ArticleDto> articleDtoList = articleService.getArticles(new PageNumber(1), new Keyword("test"));

        //then
        verify(articleRepository, never()).findAll((Pageable) any());
        verify(articleRepository).findByTitleContains(any(), stringArgumentCaptor.capture());
        assertThat("test").isEqualTo(stringArgumentCaptor.getValue());
        assertThat(2).isEqualTo(articleDtoList.size());
        assertThat(targetArticleDto.getId()).isEqualTo(articleDtoList.get(0).getId());
        assertThat(targetArticleDto.getAuthor()).isEqualTo(articleDtoList.get(0).getAuthor());
        assertThat(targetArticleDto.getContent()).isEqualTo(articleDtoList.get(0).getContent());
        assertThat(targetArticleDto.getTitle()).isEqualTo(articleDtoList.get(0).getTitle());
        assertThat(2L).isEqualTo(articleDtoList.get(1).getId());

    }

    @Test
    @DisplayName("게시글 등록 요청시, 게시글을 등록시킨다.")
    void createArticle() {

        //given
        given(articleRepository.save(any()))
                .willReturn(article);

        //when
        ArticleDto articleDto = articleService.createArticle(new SubmitArticleRequest("test", "test", "test", "test"));

        //then
        verify(articleRepository).save(any());
        assertThat(targetArticleDto.getId()).isEqualTo(articleDto.getId());
        assertThat(targetArticleDto.getAuthor()).isEqualTo(articleDto.getAuthor());
        assertThat(targetArticleDto.getContent()).isEqualTo(articleDto.getContent());
        assertThat(targetArticleDto.getTitle()).isEqualTo(articleDto.getTitle());

    }

    @Test
    @DisplayName("신고내역이 없는 게시글 삭제 요청시, 해당 게시글을 삭제한다.")
    void deleteArticle() {

        //given
        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));
        given(reportArticleRepository.findByArticle(any()))
                .willReturn(Optional.empty());

        //when
        articleService.deleteArticle(new DeleteArticleRequest(1L, "1234"));

        //then
        verify(articleRepository).delete(any());
        verify(reportArticleRepository).findByArticle(any());
        verify(reportArticleRepository, never()).delete(any());

    }

    @Test
    @DisplayName("신고내역이 있는 게시글 삭제 요청시, 해당 게시글과 요청을 삭제한다.")
    void deleteArticleWithReport() {

        //given
        ReportArticle reportArticle = ReportArticle.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .reportedCount(1)
                .article(article)
                .build();

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));
        given(reportArticleRepository.findByArticle(any()))
                .willReturn(Optional.of(reportArticle));

        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);
        ArgumentCaptor<ReportArticle> reportArticleArgumentCaptor = ArgumentCaptor.forClass(ReportArticle.class);

        //when
        articleService.deleteArticle(new DeleteArticleRequest(1L, "1234"));

        //then
        verify(articleRepository).delete(articleArgumentCaptor.capture());
        verify(reportArticleRepository).findByArticle(articleArgumentCaptor.capture());
        verify(reportArticleRepository).delete(reportArticleArgumentCaptor.capture());

        List<Article> articleList = articleArgumentCaptor.getAllValues();

        for (Article articleCapture : articleList) {
            assertThat(article.getId()).isEqualTo(articleCapture.getId());
        }

    }

    @Test
    @DisplayName("게시글 삭제 요청시, 게시글이 존재하지 않을 경우 예외를 발생시킨다.")
    void deleteArticleFail_ArticleNotFound() {

        //given
        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> articleService.deleteArticle(new DeleteArticleRequest(1L, "1234")));

        //then
        verify(articleRepository).findById(anyLong());
        assertThat(ErrorCode.ARTICLE_NOT_FOUND).isEqualTo(articleException.getErrorCode());

    }

    @Test
    @DisplayName("게시글 삭제 요청시, 비밀번호가 일치하지 않을 경우 예외를 발생시킨다.")
    void deleteArticleFail_PasswordNotCorrect() {

        //given
        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));

        doThrow(new ArticleException(ErrorCode.NO_PERMISSION)).when(passwordUtil).isPasswordValid(anyString(), anyString());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> articleService.deleteArticle(new DeleteArticleRequest(1L, "1234")));

        //then
        verify(articleRepository).findById(anyLong());
        assertThat(ErrorCode.NO_PERMISSION).isEqualTo(articleException.getErrorCode());

    }
}
