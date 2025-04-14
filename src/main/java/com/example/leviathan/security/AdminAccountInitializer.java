package com.example.leviathan.security;

import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Role;
import com.example.leviathan.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.memberId}")
    private String adminMemberId;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (!memberRepository.existsByMemberId(adminMemberId)) {
            Member admin = new Member(
                    adminMemberId,
                    passwordEncoder.encode(adminPassword)
            );

            try {
                java.lang.reflect.Field roleField = Member.class.getDeclaredField("role");
                roleField.setAccessible(true);
                roleField.set(admin, Role.ROLE_ADMIN);
            } catch (Exception e) {
                System.out.println("⚠️ 관리자 역할 설정 실패: " + e.getMessage());
            }

            memberRepository.save(admin);
            System.out.println("✅ 관리자 계정 자동 생성 완료: " + adminMemberId);
        } else {
            System.out.println("ℹ️ 관리자 계정 이미 존재합니다.");
        }
    }
}
