package com.example.leviathan.service;


import com.example.leviathan.domain.Member;
import com.example.leviathan.dto.LoginDTO;
import com.example.leviathan.dto.SaveProblemDTO;
import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;
    private final PasswordService passwordService;

    //토큰으로 멤버객체 가져옴
    public Member tokenToMember(HttpServletRequest request) {
        Member member = findByMemberId(jwtUtility.getMemberId(jwtUtility.resolveToken(request)));

        if (member != null) {
            return member;
        } else {
            throw new IllegalArgumentException("로그인을 해주세요.");
        }
    }

    //멤버 아이디로 멤버 찾기
    public Member findByMemberId(String memberId) {
        try {
            Member member = memberRepository.findByMemberId(memberId);
            if (member == null) {
                throw new IllegalArgumentException("아이디가 올바른지 확인해주세요.");
            }
            return member;
        } catch (Exception e) {
            throw new RuntimeException("회원 정보 조회 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public ResponseEntity<String> signUp(SignUpDTO dto) {

        try {
            // 중복 확인
            Member member = memberRepository.findByMemberId(dto.getMemberId());
            if (member != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("이미 사용 중인 회원 ID입니다.");
            } else {
                // 비밀번호 암호화
                String encryptedPassword = passwordService.encodePassword(dto.getPassword());

                // 새로운 회원 저장 (암호화된 비밀번호로 저장)
                memberRepository.save(new Member(dto.getMemberId(), encryptedPassword));

                return ResponseEntity.ok("회원가입 성공");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 중 오류가 발생했습니다.");
        }
    }



    public ResponseEntity<String> login(LoginDTO dto) {
        // 사용자 ID로 회원 정보를 찾음
        Member member = memberRepository.findByMemberId(dto.getMemberId());

        // 회원이 존재하고, 비밀번호가 일치하는지 확인
        if (member != null && passwordService.matches(dto.getPassword(), member.getPassword())) {
            // 비밀번호가 맞다면 JWT 토큰을 생성
            String token = jwtUtility.generateToken(dto.getMemberId());
            return ResponseEntity.ok(token);
        } else {
            // 비밀번호 불일치 또는 회원이 없는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("로그인 오류!! 아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }




    @Transactional
    public ResponseEntity<String> deleteMember(Long memberId){
        // 1. 해당 memberId로 멤버가 존재하는지 확인
        Optional<Member> member = memberRepository.findById(memberId);

        if (!member.isPresent()) {
            // 멤버가 존재하지 않으면 404 오류 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        // 2. 멤버가 존재하면 삭제 처리
        memberRepository.deleteById(memberId);
        // 3. 삭제 완료 후 성공 메시지 반환
        return ResponseEntity.ok("Member deleted successfully");
    }


}
