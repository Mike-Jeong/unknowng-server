package com.example.unknowngserver.admin.entity;

import com.example.unknowngserver.admin.type.AdminType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length=50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    private AdminType adminType;

    @Builder
    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
        adminType = AdminType.ROLE_UNAPPROVED;
    }
}
