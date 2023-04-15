package com.example.unknowngserver.auth.service;

import com.example.unknowngserver.admin.repository.AdminRepository;
import com.example.unknowngserver.auth.dto.LoginRequest;
import com.example.unknowngserver.exception.AuthException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.util.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisIndexedSessionRepository redisIndexedSessionRepository;
    private final SessionManager sessionManager;

    @Transactional
    public void login(LoginRequest loginRequest) {

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new AuthException(ErrorCode.LOGIN_FAILED);
        }
    }

    @Transactional
    public void logout() {

        HttpSession session = sessionManager.getSession()
                .orElseThrow(() -> new AuthException(ErrorCode.NO_LOGIN_INFORMATION_FOUND));

        String sessionId = session.getId();

        session.invalidate();
        redisIndexedSessionRepository.deleteById(sessionId);


    }
}
