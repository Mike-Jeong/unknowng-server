package com.example.unknowngserver.admin.controller;

import com.example.unknowngserver.admin.dto.SignUpAdminRequest;
import com.example.unknowngserver.admin.service.AdminService;
import com.example.unknowngserver.common.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<Response> submitArticle(@RequestBody @Valid SignUpAdminRequest signUpAdminRequest) {

        adminService.signUp(signUpAdminRequest);

        return ResponseEntity.ok().body(Response.ok());
    }
}
