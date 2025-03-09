package com.example.leviathan.controller;


import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO dto){
        System.out.println(dto);
        return memberService.signUp(dto);
    }


}
