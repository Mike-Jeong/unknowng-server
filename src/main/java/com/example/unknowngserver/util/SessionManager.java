package com.example.unknowngserver.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class SessionManager {

    public Optional<HttpSession> getSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return Optional.ofNullable(servletRequestAttributes.getRequest().getSession(false));
    }
}
