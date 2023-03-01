package com.example.unknowngserver.common.dto;

import lombok.Getter;

@Getter
public class Keyword {

    private String keyword;

    public Keyword(String keyword) {
        this.keyword = keyword;
    }
}
