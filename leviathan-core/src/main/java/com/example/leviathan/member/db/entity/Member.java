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

    // 일반 회원 생성 (password는 암호화된 값)
    public Member(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

    // 관리자 계정 생성
    public static Member createAdmin(String memberId, String password) {
        Member admin = new Member(memberId, password);
        admin.role = Role.ROLE_ADMIN;
        return admin;
    }

    public boolean isAdmin() {
        return role == Role.ROLE_ADMIN;
    }

    // 동일 회원 여부 (작성자 확인 등에 사용)
    public boolean isSameMember(Member other) {
        return other != null && id != null && id.equals(other.getId());
    }
}
