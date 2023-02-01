package com.example.unknowngserver.exception.dto;

import com.example.unknowngserver.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private ErrorCode errorCode;
    private String errorMessage;
}
