package com.example.unknowngserver.util;

import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.exception.PasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder;

    public void isPasswordValid(String password, String encodedPassword) {

        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new PasswordException(ErrorCode.NO_PERMISSION);
        }
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
