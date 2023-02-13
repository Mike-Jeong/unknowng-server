package com.example.unknowngserver.article.service;

import com.example.unknowngserver.article.dto.ArticleDto;
import com.example.unknowngserver.article.dto.DeleteArticleRequest;
import com.example.unknowngserver.article.dto.SubmitArticleRequest;
import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.exception.ArticleException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.report.entity.ReportArticle;
import com.example.unknowngserver.report.repository.ReportArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ReportArticleRepository reportArticleRepository;

    private static final MockedStatic<BCrypt> bCryptMockedStatic = mockStatic(BCrypt.class);

    @InjectMocks
    private ArticleService articleService;

    @Test
    @DisplayName("게시글 조회 API 서비스 테스트 성공")
    void getArticle() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));

        //when
        ArticleDto articleDto = articleService.getArticle(1L);

        //then
        verify(articleRepository).findById(anyLong());
        assertEquals(1L, articleDto.getId());
        assertEquals("test", articleDto.getTitle());
        assertEquals("테스트용", articleDto.getContent());
        assertEquals("test", articleDto.getAuthor());

    }

    @Test
    @DisplayName("게시글 조회 API 서비스 테스트 실패 - 게시글이 존재하지 않음")
    void getArticleFail() {

        //given

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> articleService.getArticle(1L));

        //then
        verify(articleRepository).findById(anyLong());
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND, articleException.getErrorCode());

    }

    @Test
    @DisplayName("게시글 리스트 조회 API 서비스 테스트 성공 - 게시글 전체 리스트")
    void getArticles() {

        //given
        Article article1 = Article.builder()
                .id(1L)
                .title("test1")
                .content("테스트용1")
                .author("test1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();
        Article article2 = Article.builder()
                .id(2L)
                .title("test2")
                .content("테스트용2")
                .author("test2")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();
        Article article3 = Article.builder()
                .id(3L)
                .title("test3")
                .content("테스트용3")
                .author("test3")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        List<Article> articleList = new ArrayList<>();
        articleList.add(article1);
        articleList.add(article2);
        articleList.add(article3);

        Page<Article> articlePageList = new PageImpl<>(articleList);

        given(articleRepository.findAll((Pageable) any()))
                .willReturn(articlePageList);

        //when
        List<ArticleDto> articleDtoList = articleService.getArticles(1, "");

        //then
        verify(articleRepository).findAll((Pageable) any());
        assertEquals(3, articleDtoList.size());
        assertEquals(1L, articleDtoList.get(0).getId());
        assertEquals("test1", articleDtoList.get(0).getTitle());
        assertEquals(2L, articleDtoList.get(1).getId());
        assertEquals("test2", articleDtoList.get(1).getTitle());
        assertEquals(3L, articleDtoList.get(2).getId());
        assertEquals("test3", articleDtoList.get(2).getTitle());

    }

    @Test
    @DisplayName("게시글 리스트 조회 API 서비스 테스트 성공 - 키워드로 검색된 게시글 리스트")
    void getArticlesWithKeyword() {

        //given
        Article article1 = Article.builder()
                .id(1L)
                .title("test1")
                .content("테스트용1")
                .author("test1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();
        Article article2 = Article.builder()
                .id(2L)
                .title("test2")
                .content("테스트용2")
                .author("test2")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();
        Article article3 = Article.builder()
                .id(3L)
                .title("test3")
                .content("테스트용3")
                .author("test3")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        List<Article> articleList = new ArrayList<>();
        articleList.add(article1);
        articleList.add(article2);
        articleList.add(article3);

        Page<Article> articlePageList = new PageImpl<>(articleList);

        given(articleRepository.findByTitleContains(any(), anyString()))
                .willReturn(articlePageList);

        //when
        List<ArticleDto> articleDtoList = articleService.getArticles(1, "test");

        //then
        verify(articleRepository, never()).findAll((Pageable) any());
        verify(articleRepository).findByTitleContains(any(), anyString());
        assertEquals(3, articleDtoList.size());
        assertEquals(1L, articleDtoList.get(0).getId());
        assertEquals("test1", articleDtoList.get(0).getTitle());
        assertEquals(2L, articleDtoList.get(1).getId());
        assertEquals("test2", articleDtoList.get(1).getTitle());
        assertEquals(3L, articleDtoList.get(2).getId());
        assertEquals("test3", articleDtoList.get(2).getTitle());

    }

    @Test
    @DisplayName("게시글 등록 API 서비스 테스트 성공")
    void createArticle() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        given(articleRepository.save(any()))
                .willReturn(article);

        //when
        ArticleDto articleDto = articleService.createArticle(new SubmitArticleRequest("test", "test", "test", "test"));

        //then
        verify(articleRepository).save(any());
        assertEquals(1L, articleDto.getId());
        assertEquals("test", articleDto.getTitle());
        assertEquals("테스트용", articleDto.getContent());
        assertEquals("test", articleDto.getAuthor());

    }

    @Test
    @DisplayName("게시글 삭제 API 서비스 테스트 성공 - 해당 게시글에 신고내역이 없을때")
    void deleteArticle() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));
        given(reportArticleRepository.findByArticle(any()))
                .willReturn(Optional.empty());
        given(BCrypt.checkpw(anyString(), anyString()))
                .willReturn(true);

        //when
        boolean result = articleService.deleteArticle(new DeleteArticleRequest(1L, "1234"));

        //then
        verify(articleRepository).delete(any());
        verify(reportArticleRepository).findByArticle(any());
        verify(reportArticleRepository, never()).delete(any());
        assertTrue(result);

    }

    @Test
    @DisplayName("게시글 삭제 API 서비스 테스트 성공 - 해당 게시글에 신고내역이 있을때")
    void deleteArticleWithReport() {

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
                .reportedCount(1)
                .article(article)
                .build();

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));
        given(reportArticleRepository.findByArticle(any()))
                .willReturn(Optional.of(reportArticle));

        given(BCrypt.checkpw(anyString(), anyString()))
                .willReturn(true);

        //when
        boolean result = articleService.deleteArticle(new DeleteArticleRequest(1L, "1234"));

        //then
        verify(articleRepository).delete(any());
        verify(reportArticleRepository).findByArticle(any());
        verify(reportArticleRepository).delete(any());
        assertTrue(result);

    }

    @Test
    @DisplayName("게시글 삭제 API 서비스 테스트 실패 - 게시글이 존재하지 않음")
    void deleteArticleFail_ArticleNotFound() {

        //given

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> articleService.deleteArticle(new DeleteArticleRequest(1L, "1234")));

        //then
        verify(articleRepository).findById(anyLong());
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND, articleException.getErrorCode());

    }

    @Test
    @DisplayName("게시글 삭제 API 서비스 테스트 실패 - 비밀번호가 일치하지 않음")
    void deleteArticleFail_PasswordNotCorrect() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));

        given(BCrypt.checkpw(anyString(), anyString()))
                .willReturn(false);

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> articleService.deleteArticle(new DeleteArticleRequest(1L, "1234")));

        //then
        verify(articleRepository).findById(anyLong());
        assertEquals(ErrorCode.NO_PERMISSION, articleException.getErrorCode());

    }

}