package com.example.unknowngserver.common.dto;

import lombok.Getter;

@Getter
public class Response {

    private String code;
    private String message;

    public Response (String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Response ok() {
        return new Response("200", "성공");
    }
}
