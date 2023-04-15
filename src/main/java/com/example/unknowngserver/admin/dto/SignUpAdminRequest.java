package com.example.unknowngserver.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class SignUpAdminRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

}
