package com.example.unknowngserver.comment.service;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.article.repository.ArticleRepository;
import com.example.unknowngserver.comment.dto.CommentDto;
import com.example.unknowngserver.comment.dto.DeleteCommentRequest;
import com.example.unknowngserver.comment.dto.SubmitCommentRequest;
import com.example.unknowngserver.comment.entity.Comment;
import com.example.unknowngserver.comment.repository.CommentRepository;
import com.example.unknowngserver.common.dto.PageNumber;
import com.example.unknowngserver.exception.ArticleException;
import com.example.unknowngserver.exception.CommentException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.report.entity.ReportComment;
import com.example.unknowngserver.report.repository.ReportCommentRepository;
import com.example.unknowngserver.util.PasswordUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
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

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private CommentService commentService;

    Article article;
    Comment comment1, comment2, comment3;
    List<Comment> commentList;

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

        comment1 = Comment.builder()
                .id(1L)
                .content("comment test 1")
                .author("ctest1")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .article(article)
                .build();
        comment2 = Comment.builder()
                .id(2L)
                .content("comment test 2")
                .author("ctest2")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .article(article)
                .build();
        comment3 = Comment.builder()
                .id(3L)
                .content("comment test 3")
                .author("ctest3")
                .password("1234")
                .registeredAt(LocalDateTime.now())
                .article(article)
                .build();

        commentList = new ArrayList<>();

        commentList.add(comment1);
        commentList.add(comment2);
        commentList.add(comment3);
    }

    @Test
    @DisplayName("게시글의 댓글 목록 조회 요청시, 댓글 목록을 가져온다")
    void getComments() {

        //given
        Page<Comment> commentPageList = new PageImpl<>(commentList);

        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));
        given(commentRepository.findAllByArticle(any(), any()))
                .willReturn(commentPageList);

        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);

        //when
        List<CommentDto> commentDtoList = commentService.getComments(1L, new PageNumber(1));

        //then
        verify(articleRepository).findById(anyLong());
        verify(commentRepository).findAllByArticle(articleArgumentCaptor.capture(), any());
        assertThat(3).isEqualTo(commentDtoList.size());
        assertThat(1L).isEqualTo(commentDtoList.get(0).getId());
        assertThat("comment test 1").isEqualTo(commentDtoList.get(0).getContent());
        assertThat("ctest1").isEqualTo(commentDtoList.get(0).getAuthor());
        assertThat(2L).isEqualTo(commentDtoList.get(1).getId());
        assertThat("comment test 2").isEqualTo(commentDtoList.get(1).getContent());
        assertThat("ctest2").isEqualTo(commentDtoList.get(1).getAuthor());
        assertThat(3L).isEqualTo(commentDtoList.get(2).getId());
        assertThat("comment test 3").isEqualTo(commentDtoList.get(2).getContent());
        assertThat("ctest3").isEqualTo(commentDtoList.get(2).getAuthor());
        assertThat(article.getId()).isEqualTo(articleArgumentCaptor.getValue().getId());
        assertThat(article.getAuthor()).isEqualTo(articleArgumentCaptor.getValue().getAuthor());
    }

    @Test
    @DisplayName("게시글의 댓글 목록 조회 요청시, 대상 게시글이 존재하지 않을 경우 예외를 발생시킨다.")
    void getCommentsFail_ArticleNotFound() {

        //given
        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> commentService.getComments(1L, any()));

        //then
        verify(articleRepository).findById(anyLong());
        assertThat(ErrorCode.ARTICLE_NOT_FOUND).isEqualTo(articleException.getErrorCode());
    }

    @Test
    @DisplayName("댓글 등록 요청시, 댓글을 등록시킨다.")
    void createComment() {

        //given
        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        //when
        commentService.createComment(new SubmitCommentRequest(1L, "comment test 1", "ctest1", "1234"));

        //then
        verify(articleRepository).findById(any());
        verify(commentRepository).save(commentArgumentCaptor.capture());
        assertThat("comment test 1").isEqualTo(commentArgumentCaptor.getValue().getContent());
        assertThat("ctest1").isEqualTo(commentArgumentCaptor.getValue().getAuthor());

    }

    @Test
    @DisplayName("댓글 등록 요청시, 대상 게시글이 존재 하지 않을 경우 예외를 발생시킨다.")
    void createCommentFail_ArticleNotFound() {

        //given
        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ArticleException articleException = assertThrows(ArticleException.class,
                () -> commentService.createComment(new SubmitCommentRequest(1L, "comment test 1", "ctest1", "1234")));

        //then
        verify(articleRepository).findById(1L);
        assertThat(ErrorCode.ARTICLE_NOT_FOUND).isEqualTo(articleException.getErrorCode());
    }

    @Test
    @DisplayName("신고내역이 없는 댓글 삭제 요청시, 해당 댓을 삭제한다.")
    void deleteComment() {

        //given
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment1));
        given(reportCommentRepository.findByComment(comment1))
                .willReturn(Optional.empty());

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        //when
        commentService.deleteComment(new DeleteCommentRequest(1L, "1234"));

        //then
        verify(commentRepository).findById(1L);
        verify(commentRepository).delete(commentArgumentCaptor.capture());
        verify(reportCommentRepository).findByComment(commentArgumentCaptor.capture());
        verify(reportCommentRepository, never()).delete(any());

        List<Comment> commentList = commentArgumentCaptor.getAllValues();

        for (Comment commentCapture : commentList) {
            assertThat(comment1.getId()).isEqualTo(commentCapture.getId());
        }

    }

    @Test
    @DisplayName("신고내역이 있는 댓글 삭제 요청시, 해당 댓과 요청을 삭제한다.")
    void deleteCommentWithReport() {

        //given
        ReportComment reportComment = ReportComment.builder()
                .id(1L)
                .firstReportedAt(LocalDateTime.now())
                .reportedCount(1)
                .comment(comment1)
                .build();

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment1));
        given(reportCommentRepository.findByComment(comment1))
                .willReturn(Optional.of(reportComment));

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        ArgumentCaptor<ReportComment> reportCommentArgumentCaptor = ArgumentCaptor.forClass(ReportComment.class);

        //when
        commentService.deleteComment(new DeleteCommentRequest(1L, "1234"));

        //then
        verify(commentRepository).findById(1L);
        verify(commentRepository).delete(commentArgumentCaptor.capture());
        verify(reportCommentRepository).findByComment(commentArgumentCaptor.capture());
        verify(reportCommentRepository).delete(reportCommentArgumentCaptor.capture());

        assertThat(reportComment.getId()).isEqualTo(reportCommentArgumentCaptor.getValue().getId());

        List<Comment> commentList = commentArgumentCaptor.getAllValues();

        for (Comment commentCapture : commentList) {
            assertThat(comment1.getId()).isEqualTo(commentCapture.getId());
        }
    }

    @Test
    @DisplayName("댓글 삭제 요청시, 댓이 존재하지 않을 경우 예외를 발생시킨다.")
    void deleteCommentFail_CommentNotFound() {

        //given
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        CommentException commentException = assertThrows(CommentException.class,
                () -> commentService.deleteComment(new DeleteCommentRequest(1L, "1234")));

        //then
        verify(commentRepository).findById(1L);
        assertThat(ErrorCode.COMMENT_NOT_FOUND).isEqualTo(commentException.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 요청시, 비밀번호가 일치하지 않을 경우 예외를 발생시킨다.")
    void deleteCommentFail_PasswordNotCorrect() {

        //given
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment1));

        doThrow(new CommentException(ErrorCode.NO_PERMISSION)).when(passwordUtil).isPasswordValid(anyString(), anyString());

        //when
        CommentException commentException = assertThrows(CommentException.class,
                () -> commentService.deleteComment(new DeleteCommentRequest(1L, "1234")));

        //then
        verify(commentRepository).findById(1L);
        assertThat(ErrorCode.NO_PERMISSION).isEqualTo(commentException.getErrorCode());
    }

}
