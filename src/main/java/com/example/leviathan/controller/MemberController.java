package com.example.leviathan.controller;


import com.example.leviathan.dto.LoginDTO;
import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // React에서 API 요청 허용
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


    @GetMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto){

        return memberService.login(dto);
    }

}
