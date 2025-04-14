package com.example.leviathan.controller;


import com.example.leviathan.dto.LoginDTO;
import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;




    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO dto){
        return memberService.signUp(dto);
    }

    @DeleteMapping("/deleteMember/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId){

        // 토근 검증 로직도 나중에 구현 현재는 개발중이라 보류
        // memberService에서 삭제 로직 수행
        return memberService.deleteMember(memberId);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO dto){
        return memberService.login(dto);
    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {


        memberService.logout(request);

        // "accessToken"과 같은 이름으로 저장된 쿠키를 삭제
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)  // HTTPS 환경에서만 전송 (테스트 시 false 가능)
                .path("/")  // 전체 경로에 대해 쿠키 삭제
                .maxAge(0)  // 쿠키의 만료 시간을 0으로 설정하여 삭제
                .sameSite("Strict")
                .build();

        // 응답 헤더에 쿠키 삭제 정보 추가
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        // 로그아웃 성공 메시지 응답
        return ResponseEntity.ok("로그아웃 성공");
    }

}
