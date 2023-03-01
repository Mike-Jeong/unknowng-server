package com.example.unknowngserver.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentInfo {
    private Long id;
    private String author;
    private String content;
    private LocalDateTime createDate;

    @Builder
    public CommentInfo(Long id, String author, String content, LocalDateTime createDate) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createDate = createDate;
    }

    public static CommentInfo fromCommentDto(CommentDto commentDto) {
        return CommentInfo.builder()
                .id(commentDto.getId())
                .author(commentDto.getAuthor())
                .content(commentDto.getContent())
                .createDate(commentDto.getRegisteredAt())
                .build();
    }
}
