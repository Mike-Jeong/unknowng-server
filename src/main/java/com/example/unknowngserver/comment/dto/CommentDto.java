package com.example.unknowngserver.comment.dto;

import com.example.unknowngserver.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime registeredAt;

    @Builder
    public CommentDto(Long id, String content, String author, LocalDateTime registeredAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.registeredAt = registeredAt;
    }

    public static CommentDto fromComment(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor())
                .registeredAt(comment.getRegisteredAt())
                .build();
    }
}
