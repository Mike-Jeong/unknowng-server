package com.example.unknowngserver.common.dto;

import lombok.Getter;

@Getter
public class PageNumber {

    private Integer page;

    public PageNumber(Integer page) {
        this.page = validateRequestPage(page);
    }

    private int validateRequestPage(Integer page){
        return page == null ? 0 : Math.max(page - 1, 0);
    }
}
