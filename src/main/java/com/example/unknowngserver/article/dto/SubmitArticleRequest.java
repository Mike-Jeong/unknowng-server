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

    /*@Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ServiceDto {
        private String title;
        private String article;
        private String author;
        private String password;
    }


    public static ServiceDto toServiceDto(SubmitArticleRequest submitArticleRequest) {
        return ServiceDto.builder()
                .title(submitArticleRequest.getTitle())
                .article(submitArticleRequest.getArticle())
                .author(submitArticleRequest.getAuthor())
                .password(submitArticleRequest.getPassword())
                .build();
    }*/
}
