package com.example.unknowngserver.comment.service;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.comment.dto.CommentDto;
import com.example.unknowngserver.comment.dto.DeleteCommentRequest;
import com.example.unknowngserver.comment.dto.SubmitCommentRequest;
import com.example.unknowngserver.comment.entity.Comment;
import com.example.unknowngserver.comment.repository.CommentRepository;
import com.example.unknowngserver.exception.ArticleException;
import com.example.unknowngserver.exception.CommentException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.report.entity.ReportComment;
import com.example.unknowngserver.report.repository.ReportCommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ReportCommentRepository reportCommentRepository;

    private static final MockedStatic<BCrypt> bCryptMockedStatic = mockStatic(BCrypt.class);

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 조회 API 서비스 테스트 성공")
    void getComments() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        Comment comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .article(article)
                .build();
        Comment comment2 = Comment.builder()
                .id(2L)
                .content("comment test 2")
                .author("ctest2")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .article(article)
                .build();
        Comment comment3 = Comment.builder()
                .id(3L)
                .content("comment test 3")
                .author("ctest3")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .article(article)
                .build();

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment2);
        commentList.add(comment3);

        Page<Comment> commentPageList = new PageImpl<>(commentList);

        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));
        given(commentRepository.findAllByArticle(any(), any()))
                .willReturn(commentPageList);

        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);

        //when
        List<CommentDto> commentDtoList = commentService.getComments(1L, 1);

        //then
        verify(articleRepository).findById(anyLong());
        verify(commentRepository).findAllByArticle(articleArgumentCaptor.capture(), any());
        assertEquals(3, commentDtoList.size());
        assertEquals(1L, commentDtoList.get(0).getId());
        assertEquals("comment test 1", commentDtoList.get(0).getContent());
        assertEquals("ctest1", commentDtoList.get(0).getAuthor());
        assertEquals(2L, commentDtoList.get(1).getId());
        assertEquals("comment test 2", commentDtoList.get(1).getContent());
        assertEquals("ctest2", commentDtoList.get(1).getAuthor());
        assertEquals(3L, commentDtoList.get(2).getId());
        assertEquals("comment test 3", commentDtoList.get(2).getContent());
        assertEquals("ctest3", commentDtoList.get(2).getAuthor());
        assertEquals(article.getId(), articleArgumentCaptor.getValue().getId());
        assertEquals(article.getAuthor(), articleArgumentCaptor.getValue().getAuthor());
    }

    @Test
    @DisplayName("댓글 조회 API 서비스 테스트 성공")
    void getCommentsFail_ArticleNotFound() {

        //given

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> commentService.getComments(1L, 1));

        //then
        verify(articleRepository).findById(anyLong());
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND, articleException.getErrorCode());
    }

    @Test
    @DisplayName("댓글 등록 API 서비스 테스트 성공")
    void createComment() {

        //given
        Article article = Article.builder()
                .id(1L)
                .title("test")
                .content("테스트용")
                .author("test")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));
        given(BCrypt.checkpw(anyString(), anyString()))
                .willReturn(true);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        //when
        boolean result = commentService.createComment(new SubmitCommentRequest(1L, "comment test 1", "ctest1", "1234"));

        //then
        verify(articleRepository).findById(any());
        verify(commentRepository).save(commentArgumentCaptor.capture());
        assertEquals("comment test 1", commentArgumentCaptor.getValue().getContent());
        assertEquals("ctest1", commentArgumentCaptor.getValue().getAuthor());
        assertTrue(result);
    }

    @Test
    @DisplayName("댓글 등록 API 서비스 테스트 실패 - 대상 게시글을 찾을 수 없음")
    void createCommentFail_ArticleNotFound() {

        //given

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> commentService.createComment(new SubmitCommentRequest(1L, "comment test 1", "ctest1", "1234")));

        //then
        verify(articleRepository).findById(1L);
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND, articleException.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 API 서비스 테스트 성공 - 해당 게시글에 신고내역이 없을때")
    void deleteComment() {

        //given
        Comment comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment1));
        given(BCrypt.checkpw(anyString(), anyString()))
                .willReturn(true);
        given(reportCommentRepository.findByComment(comment1))
                .willReturn(Optional.empty());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        //when
        boolean result = commentService.deleteComment(new DeleteCommentRequest(1L, "1234"));

        //then
        verify(commentRepository).findById(1L);
        verify(commentRepository).delete(commentArgumentCaptor.capture());
        verify(reportCommentRepository).findByComment(commentArgumentCaptor.capture());
        verify(reportCommentRepository, never()).delete(any());

        assertTrue(result);

        List<Comment> commentList = commentArgumentCaptor.getAllValues();

        for (Comment commentCapture : commentList) {
            assertEquals(comment1, commentCapture);
        }

    }

    @Test
    @DisplayName("댓글 삭제 API 서비스 테스트 성공 - 해당 게시글에 신고내역이 있을때")
    void deleteCommentWithReport() {

        //given
        Comment comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        ReportComment reportComment = ReportComment.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .reportedCount(1)
                .comment(comment1)
                .build();

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment1));
        given(BCrypt.checkpw(anyString(), anyString()))
                .willReturn(true);
        given(reportCommentRepository.findByComment(comment1))
                .willReturn(Optional.of(reportComment));

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        ArgumentCaptor<ReportComment> reportCommentArgumentCaptor = ArgumentCaptor.forClass(ReportComment.class);

        //when
        boolean result = commentService.deleteComment(new DeleteCommentRequest(1L, "1234"));

        //then
        verify(commentRepository).findById(1L);
        verify(commentRepository).delete(commentArgumentCaptor.capture());
        verify(reportCommentRepository).findByComment(commentArgumentCaptor.capture());
        verify(reportCommentRepository).delete(reportCommentArgumentCaptor.capture());

        assertTrue(result);
        assertEquals(reportComment, reportCommentArgumentCaptor.getValue());

        List<Comment> commentList = commentArgumentCaptor.getAllValues();

        for (Comment commentCapture : commentList) {
            assertEquals(comment1, commentCapture);
        }
    }

    @Test
    @DisplayName("댓글 삭제 API 서비스 테스트 실패 - 댓글이 존재하지 않음")
    void deleteCommentFail_CommentNotFound() {

        //given
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        CommentException commentException = assertThrows(CommentException.class,
                () -> commentService.deleteComment(new DeleteCommentRequest(1L, "1234")));

        //then
        verify(commentRepository).findById(1L);
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, commentException.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 API 서비스 테스트 실패 - 비밀번호가 일치하지 않음")
    void deleteCommentFail_PasswordNotCorrect() {

        //given
        Comment comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .build();

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment1));

        given(BCrypt.checkpw(anyString(), anyString()))
                .willReturn(false);

        //when
        CommentException commentException = assertThrows(CommentException.class,
                () -> commentService.deleteComment(new DeleteCommentRequest(1L, "1234")));

        //then
        verify(commentRepository).findById(1L);
        assertEquals(ErrorCode.NO_PERMISSION, commentException.getErrorCode());
    }

}