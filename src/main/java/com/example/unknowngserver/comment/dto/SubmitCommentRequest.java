package com.example.unknowngserver.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NotNull
public class SubmitCommentRequest {

    private Long articleId;
    private String title;
    private String content;
    private String author;
    private String password;
}
