package com.example.leviathan.common.security;

import com.example.leviathan.common.JwtUtility;
import com.example.leviathan.common.TokenRefreshService;
import com.example.leviathan.common.exception.UnauthorizedException;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.db.entity.RefreshToken;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.member.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenRefreshServiceTest {

    @Mock
    private JwtUtility jwtUtility;

    @Mock
    private MemberService memberService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private TokenRefreshService tokenRefreshService;

    private final Member member = new Member("testUser", "encrypted");

    @Test
    @DisplayName("RefreshToken이 유효하면 AccessToken을 재발급하고 RefreshToken을 교체한다")
    void reissue_Success() {
        RefreshToken refreshToken = RefreshToken.issue(member, "oldRefresh", LocalDateTime.now().plusMinutes(30));

        when(jwtUtility.getMemberIdFromToken("expiredAccess")).thenReturn("testUser");
        when(memberService.findByLoginId("testUser")).thenReturn(member);
        when(refreshTokenService.findByMember(member)).thenReturn(Optional.of(refreshToken));
        when(jwtUtility.isTokenValid("oldRefresh")).thenReturn(true);
        when(jwtUtility.generateRefreshToken("testUser")).thenReturn("newRefresh");
        when(jwtUtility.generateToken("testUser", member.getRole())).thenReturn("newAccess");

        String newAccessToken = tokenRefreshService.reissueAccessToken("expiredAccess");

        assertEquals("newAccess", newAccessToken);
        verify(refreshTokenService, times(1)).rotate(eq(refreshToken), eq("newRefresh"), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("RefreshToken이 없으면(로그아웃 상태) 재발급하지 않는다")
    void reissue_Fail_NoRefreshToken() {
        when(jwtUtility.getMemberIdFromToken("expiredAccess")).thenReturn("testUser");
        when(memberService.findByLoginId("testUser")).thenReturn(member);
        when(refreshTokenService.findByMember(member)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> tokenRefreshService.reissueAccessToken("expiredAccess"));
        verify(jwtUtility, never()).generateToken(any(), any());
    }

    @Test
    @DisplayName("RefreshToken이 만료되었으면 재발급하지 않는다")
    void reissue_Fail_ExpiredRefreshToken() {
        RefreshToken refreshToken = RefreshToken.issue(member, "oldRefresh", LocalDateTime.now().minusMinutes(1));

        when(jwtUtility.getMemberIdFromToken("expiredAccess")).thenReturn("testUser");
        when(memberService.findByLoginId("testUser")).thenReturn(member);
        when(refreshTokenService.findByMember(member)).thenReturn(Optional.of(refreshToken));

        assertThrows(UnauthorizedException.class, () -> tokenRefreshService.reissueAccessToken("expiredAccess"));
        verify(jwtUtility, never()).generateToken(any(), any());
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 재발급하지 않는다")
    void reissue_Fail_NoMember() {
        when(jwtUtility.getMemberIdFromToken("expiredAccess")).thenReturn("deletedUser");
        when(memberService.findByLoginId("deletedUser")).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> tokenRefreshService.reissueAccessToken("expiredAccess"));
    }
}
