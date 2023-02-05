package com.example.unknowngserver.comment.repository;

import com.example.unknowngserver.article.entity.Article;
import com.example.unknowngserver.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByArticle(Article article, Pageable pageable);
}
