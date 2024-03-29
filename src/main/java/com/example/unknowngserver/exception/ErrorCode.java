package com.example.unknowngserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ARTICLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다."),
    REPORT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 신고 내역을 찾을 수 없습니다."),

    REPORT_DETAIL_CONTENT_TYPE_NOT_SUPPORTED(HttpStatus.EXPECTATION_FAILED, "해당 리포트 컨텐츠 타입이 지원되지 않습니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN,"요청에 대한 권한이 없습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    NO_LOGIN_INFORMATION_FOUND(HttpStatus.UNAUTHORIZED, "로그인 정보가 없습니다."),
    ADMIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 관리자를 찾을 수 없습니다."),
    EMAIL_ALREADY_USE(HttpStatus.BAD_REQUEST, "요청하신 이메일이 이미 사용 중 입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
