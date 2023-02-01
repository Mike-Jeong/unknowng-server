package com.example.unknowngserver.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NotNull
public class DeleteCommentRequest {

    private Long commentId;
    private String password;
}
