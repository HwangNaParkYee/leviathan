package com.example.leviathan.controller;


import com.example.leviathan.dto.LoginDTO;
import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/getMemberId")
    public ResponseEntity<String> getMemberId(HttpServletRequest header) {
        return memberService.tokenToMemberId(header);
    }


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
    public ResponseEntity<String> login(@RequestBody LoginDTO dto){

        System.out.println( dto.getMemberId() + " " + dto.getPassword());
        return memberService.login(dto);
    }

}
