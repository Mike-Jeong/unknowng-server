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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final ReportCommentRepository reportCommentRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long articleId, Integer page) {

        Article article = findArticle(articleId);

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        Page<Comment> commentPageList = commentRepository.findAllByArticle(article, pageRequest);

        return commentPageList.stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean createComment(SubmitCommentRequest submitCommentRequest) {

        Article article = findArticle(submitCommentRequest.getArticleId());

        String encPassword = BCrypt.hashpw(submitCommentRequest.getPassword(), BCrypt.gensalt());

        Comment comment = Comment.builder()
                .content(submitCommentRequest.getContent())
                .author(submitCommentRequest.getAuthor())
                .password(encPassword)
                .article(article)
                .registeredAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        return true;
    }

    @Transactional
    public boolean deleteComment(DeleteCommentRequest deleteCommentRequest) {

        Comment comment = commentRepository.findById(deleteCommentRequest.getCommentId())
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        if (!BCrypt.checkpw(deleteCommentRequest.getPassword(), comment.getPassword())) {
            throw new CommentException(ErrorCode.NO_PERMISSION);
        }

        Optional<ReportComment> reportComment = reportCommentRepository.findByComment(comment);
        reportComment.ifPresent(reportCommentRepository::delete);

        commentRepository.delete(comment);
        return true;
    }

    private Article findArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorCode.ARTICLE_NOT_FOUND));
    }
}
