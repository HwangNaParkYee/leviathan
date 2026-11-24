package com.example.leviathan.member.db.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("일반 회원은 ROLE_MEMBER 권한만 가진다")
    void member_Authorities() {
        Member member = new Member("testUser", "encrypted");

        assertFalse(member.isAdmin());
        assertEquals(List.of("ROLE_MEMBER"), member.getRole().getAuthorityNames());
    }

    @Test
    @DisplayName("관리자는 ROLE_ADMIN과 ROLE_MEMBER 권한을 함께 가진다")
    void admin_Authorities() {
        Member admin = Member.createAdmin("admin", "encrypted");

        assertTrue(admin.isAdmin());
        assertEquals(List.of("ROLE_ADMIN", "ROLE_MEMBER"), admin.getRole().getAuthorityNames());
    }

    @Test
    @DisplayName("id가 같으면 동일 회원이다")
    void isSameMember() {
        Member member = memberWithId(1L);

        assertTrue(member.isSameMember(memberWithId(1L)));
        assertFalse(member.isSameMember(memberWithId(2L)));
        assertFalse(member.isSameMember(null));
    }

    @Test
    @DisplayName("RefreshToken 교체 시 토큰과 만료일이 바뀐다")
    void refreshToken_Rotate() {
        Member member = new Member("testUser", "encrypted");
        RefreshToken token = RefreshToken.issue(member, "old", LocalDateTime.of(2026, 1, 1, 0, 0));

        LocalDateTime newExpiry = LocalDateTime.of(2026, 1, 2, 0, 0);
        token.rotate("new", newExpiry);

        assertEquals(member, token.getMember());
        assertEquals("new", token.getRefreshToken());
        assertEquals(newExpiry, token.getExpiryDate());
    }

    private Member memberWithId(Long id) {
        Member member = new Member("user" + id, "encrypted");
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }
}
