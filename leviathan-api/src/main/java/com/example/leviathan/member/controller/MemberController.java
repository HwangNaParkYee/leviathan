package com.example.leviathan.member.controller;


import com.example.leviathan.member.dto.LoginDTO;
import com.example.leviathan.member.dto.SignUpDTO;
import com.example.leviathan.member.facade.MemberFacade;
import com.example.leviathan.global.security.AccessTokenCookie;
import com.example.leviathan.global.security.LoginId;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberFacade memberFacade;


    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO dto){
        return memberFacade.signUp(dto);
    }

    @DeleteMapping("/deleteMember/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId){

        // 토근 검증 로직도 나중에 구현 현재는 개발중이라 보류
        // memberService에서 삭제 로직 수행
        return memberFacade.deleteMember(memberId);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO dto){
        return memberFacade.login(dto);
    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@LoginId String loginId, HttpServletResponse response) {


        memberFacade.logout(loginId);

        // "accessToken"과 같은 이름으로 저장된 쿠키를 삭제
        ResponseCookie accessTokenCookie = AccessTokenCookie.expired();

        // 응답 헤더에 쿠키 삭제 정보 추가
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        // 로그아웃 성공 메시지 응답
        return ResponseEntity.ok("로그아웃 성공");
    }

    //로그인 확인
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("isAuthenticated", false));
        }

        return ResponseEntity.ok(Map.of(
                "isAuthenticated", true,
                "username", authentication.getName()
        ));
    }
}
