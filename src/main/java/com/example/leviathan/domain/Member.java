package com.example.leviathan.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor  // Add this to generate a no-args constructor
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)  // 유니크 제약 조건 추가
    private String memberId;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_MEMBER;

    // Constructor to initialize member with memberId and password
    public Member(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

}
