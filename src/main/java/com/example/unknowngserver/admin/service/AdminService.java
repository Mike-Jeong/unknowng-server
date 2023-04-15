package com.example.unknowngserver.admin.service;

import com.example.unknowngserver.admin.dto.SignUpAdminRequest;
import com.example.unknowngserver.admin.entity.Admin;
import com.example.unknowngserver.admin.repository.AdminRepository;
import com.example.unknowngserver.exception.AdminException;
import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordUtil passwordUtil;

    @Transactional
    public void signUp(SignUpAdminRequest signUpAdminRequest) {

        emailDuplicateCheck(signUpAdminRequest.getEmail());

        String encPassword = passwordUtil.encodePassword(signUpAdminRequest.getPassword());

        adminRepository.save(Admin.builder()
                .email(signUpAdminRequest.getEmail())
                .password(encPassword)
                .build());
    }

    @Transactional(readOnly = true)
    public Admin getAdminByEmail(String email) {

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminException(ErrorCode.ADMIN_NOT_FOUND));

        return admin;
    }

    private void emailDuplicateCheck(String email) {
        if (adminRepository.existsByEmail(email)) {
            throw new AdminException(ErrorCode.EMAIL_ALREADY_USE);
        }
    }
}
