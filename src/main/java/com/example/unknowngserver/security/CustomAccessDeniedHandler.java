package com.example.unknowngserver.security;

import com.example.unknowngserver.exception.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.unknowngserver.exception.ErrorCode.NO_PERMISSION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        setResponse(response);

        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.builder()
                .status(NO_PERMISSION.getHttpStatus().value())
                .errorCode(NO_PERMISSION)
                .errorMessage(NO_PERMISSION.getErrorMessage())
                .build()));
        response.getWriter().flush();
    }

    private void setResponse(HttpServletResponse response) {
        response.setStatus(NO_PERMISSION.getHttpStatus().value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
    }

}
