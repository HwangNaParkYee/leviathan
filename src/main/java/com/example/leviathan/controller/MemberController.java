package com.example.leviathan.controller;


import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
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





}
