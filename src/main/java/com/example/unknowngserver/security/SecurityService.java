package com.example.unknowngserver.security;

import com.example.unknowngserver.admin.entity.Admin;
import com.example.unknowngserver.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

    private final AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminService.getAdminByEmail(username);

        return new User(admin.getId().toString(), admin.getPassword(),
                List.of(new SimpleGrantedAuthority(admin.getAdminType().toString())));
    }
}
