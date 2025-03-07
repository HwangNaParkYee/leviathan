package com.example.leviathan.domain;


import jakarta.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String memberId;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_MEMBER;
}
