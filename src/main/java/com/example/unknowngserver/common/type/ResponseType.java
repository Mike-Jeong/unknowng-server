package com.example.unknowngserver.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseType {

    OK(HttpStatus.OK, "성공");

    private final HttpStatus httpStatus;
    private final String responseMessage;
}
