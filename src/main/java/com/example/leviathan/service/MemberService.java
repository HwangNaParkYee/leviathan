package com.example.leviathan.service;


import com.example.leviathan.domain.Member;
import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;


    @Transactional
    public ResponseEntity<String> signUp(SignUpDTO dto){

        try{
            // 비밀번호 암호화 필요
            // 중복 확인
            Member member = memberRepository.findByMemberId((dto.getMemberId()));
            if (member != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("이미 사용 중인 회원 ID입니다.");
            } else {
                // 새로운 회원 저장
                memberRepository.save(new Member(dto.getMemberId(), dto.getPassword()));
                return ResponseEntity.ok("회원가입 성공");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 중 오류가 발생했습니다.");
        }
    }


}
