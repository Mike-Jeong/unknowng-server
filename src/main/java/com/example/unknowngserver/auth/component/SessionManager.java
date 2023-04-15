package com.example.unknowngserver.auth.component;

import com.example.unknowngserver.exception.AuthException;
import com.example.unknowngserver.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class SessionManager {

    public HttpSession getSession() {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpSession httpSession = Optional.ofNullable(servletRequestAttributes.getRequest().getSession(false))
                .orElseThrow(() -> new AuthException(ErrorCode.NO_LOGIN_INFORMATION_FOUND));

        return httpSession;
    }
}
