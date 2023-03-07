package com.example.unknowngserver.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NotNull
public class SubmitArticleRequest {

    private String title;
    private String content;
    private String author;
    private String password;
}
