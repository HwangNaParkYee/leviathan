package com.example.leviathan.member.facade;

import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;

import com.example.leviathan.global.security.JwtUtility;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.dto.LoginDTO;
import com.example.leviathan.member.dto.SignUpDTO;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.member.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 사용 설정
class MemberFacadeTest {

    @Mock
    private MemberService memberService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtility jwtUtility;

    @InjectMocks
    private MemberFacade memberFacade; // 위 가짜(Mock) 객체들을 여기에 주입함

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUp_Success() {
        // given (준비)
        SignUpDTO dto = new SignUpDTO("testUser", "password123");
        when(passwordEncoder.encode("password123")).thenReturn("encrypted");

        // when (실행)
        ResponseEntity<String> response = memberFacade.signUp(dto);

        // then (검증)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("회원가입 성공", response.getBody());

        // 암호화된 비밀번호로 가입했는지 확인
        verify(memberService, times(1)).register("testUser", "encrypted");
    }

    @Test
    @DisplayName("중복 회원가입 실패 테스트")
    void signUp_Fail_Duplicate() {
        // given
        SignUpDTO dto = new SignUpDTO("duplicateUser", "1234");
        when(passwordEncoder.encode("1234")).thenReturn("encrypted");
        when(memberService.register("duplicateUser", "encrypted")).thenThrow(new MemberException(MemberErrorCode.MEMBER_ALREADY_REGISTERED));

        // when
        ResponseEntity<String> response = memberFacade.signUp(dto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("이미 사용 중인 회원 ID입니다.", response.getBody());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_Success() {
        LoginDTO dto = new LoginDTO("testUser", "password123");

        Member member = new Member("testUser", "encryptedpassword");
        when(memberService.findByLoginId("testUser")).thenReturn(member);
        when(passwordEncoder.matches(dto.getPassword(), member.getPassword())).thenReturn(true);
        when(jwtUtility.generateToken(anyString(), any())).thenReturn("accessToken");
        when(jwtUtility.generateRefreshToken(anyString())).thenReturn("refreshToken");

        ResponseEntity<Object> response = memberFacade.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("로그인 성공", response.getBody());
        assertTrue(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).startsWith("accessToken=accessToken"));

        // RefreshToken을 새로 발급했는지 확인
        verify(refreshTokenService, times(1)).issue(eq(member), eq("refreshToken"), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인 실패")
    void login_Fail_WrongPassword() {
        LoginDTO dto = new LoginDTO("testUser", "wrong");

        Member member = new Member("testUser", "encryptedpassword");
        when(memberService.findByLoginId("testUser")).thenReturn(member);
        when(passwordEncoder.matches("wrong", "encryptedpassword")).thenReturn(false);

        ResponseEntity<Object> response = memberFacade.login(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(refreshTokenService, never()).issue(any(), any(), any());
    }
}
