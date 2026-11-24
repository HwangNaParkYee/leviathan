package com.example.leviathan.global.security;

import com.example.leviathan.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements CommandLineRunner {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.memberId}")
    private String adminMemberId;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (memberService.registerAdminIfAbsent(adminMemberId, passwordEncoder.encode(adminPassword))) {
            System.out.println("✅ 관리자 계정 자동 생성 완료: " + adminMemberId);
        } else {
            System.out.println("ℹ️ 관리자 계정 이미 존재합니다.");
        }
    }
}
