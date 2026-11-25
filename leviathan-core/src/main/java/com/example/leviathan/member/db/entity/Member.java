package com.example.leviathan.member.db.entity;

import com.example.leviathan.member.db.enums.Role;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)  // 유니크 제약 조건 추가
    private String memberId;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_MEMBER;

    public Member(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

    public static Member createAdmin(String memberId, String password) {
        Member admin = new Member(memberId, password);
        admin.role = Role.ROLE_ADMIN;
        return admin;
    }

    public boolean isAdmin() {
        return role == Role.ROLE_ADMIN;
    }

    public boolean isSameMember(Member other) {
        return other != null && id != null && id.equals(other.getId());
    }
}
