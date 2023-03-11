package com.example.unknowngserver.auth.service;

import com.example.unknowngserver.admin.entity.Admin;
import com.example.unknowngserver.admin.repository.AdminRepository;
import com.example.unknowngserver.auth.dto.LoginRequest;
import com.example.unknowngserver.exception.AdminException;
import com.example.unknowngserver.exception.AuthException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AdminRepository adminRepository;

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

        HttpSession session = SessionUtil.getSession();

        if(session != null) {
            session.invalidate();
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByEmail(username)
                .orElseThrow(() -> new AdminException(ErrorCode.ADMIN_NOT_FOUND));
        return new User(admin.getId().toString(), admin.getPassword(),
                List.of(new SimpleGrantedAuthority(admin.getAdminType().toString())));
    }
}
