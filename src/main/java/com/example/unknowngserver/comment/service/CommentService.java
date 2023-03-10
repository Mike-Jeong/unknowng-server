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
import com.example.unknowngserver.report.repository.ReportCommentRepository;
import com.example.unknowngserver.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private static final int PAGE_SIZE = 10;

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final ReportCommentRepository reportCommentRepository;
    private final PasswordUtil passwordUtil;

    public List<CommentDto> getComments(Long articleId, PageNumber page) {

        Article article = findArticle(articleId);

        PageRequest pageRequest = PageRequest.of(page.getPage(), PAGE_SIZE, Sort.by("id").descending());

        Page<Comment> commentPageList = commentRepository.findAllByArticle(article, pageRequest);

        return commentPageList.stream()
                .map(CommentDto::fromComment)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createComment(SubmitCommentRequest submitCommentRequest) {

        Article article = findArticle(submitCommentRequest.getArticleId());

        String encPassword = passwordUtil.encodePassword(submitCommentRequest.getPassword());

        Comment comment = Comment.builder()
                .content(submitCommentRequest.getContent())
                .author(submitCommentRequest.getAuthor())
                .password(encPassword)
                .article(article)
                .registeredAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(DeleteCommentRequest deleteCommentRequest) {

        Comment comment = commentRepository.findById(deleteCommentRequest.getCommentId())
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        passwordUtil.isPasswordValid(deleteCommentRequest.getPassword(), comment.getPassword());

        reportCommentRepository.findByComment(comment).ifPresent(reportCommentRepository::delete);

        commentRepository.delete(comment);
    }

    private Article findArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.ARTICLE_NOT_FOUND));
    }
}
