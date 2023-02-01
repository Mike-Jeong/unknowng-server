package com.example.unknowngserver.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentInfo {
    private Long id;
    private String author;
    private String content;
    private LocalDateTime createDate;

    public static CommentInfo fromCommentDto(CommentDto commentDto) {
        return CommentInfo.builder()
                .id(commentDto.getId())
                .author(commentDto.getAuthor())
                .content(commentDto.getContent())
                .createDate(commentDto.getRegisteredAt())
                .build();
    }
}
