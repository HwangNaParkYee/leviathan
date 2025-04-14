package com.example.leviathan.service;


import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.RefreshToken;
import com.example.leviathan.dto.LoginDTO;
import com.example.leviathan.dto.SaveProblemDTO;
import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.repository.RefreshTokenRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;
    private final PasswordService passwordService;
    private final RefreshTokenRepository refreshTokenRepository;


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



    @Transactional
    public ResponseEntity<Object> login(LoginDTO dto) {
        // 사용자 ID로 회원 정보를 찾음
        Member member = memberRepository.findByMemberId(dto.getMemberId());

        // 회원이 존재하고, 비밀번호가 일치하는지 확인
        if (member != null && passwordService.matches(dto.getPassword(), member.getPassword())) {
            // 비밀번호가 맞다면 JWT 토큰을 생성

            String accessToken = jwtUtility.generateToken(dto.getMemberId(), member.getRole());
            String refreshToken = jwtUtility.generateRefreshToken(dto.getMemberId());

            refreshTokenRepository.deleteByMember(member);

            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setMember(member);
            newRefreshToken.setRefreshToken(refreshToken);
            newRefreshToken.setExpiryDate(LocalDateTime.now().plusDays(30));
            refreshTokenRepository.save(newRefreshToken);

            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60 * 24)
                    .sameSite("Strict")
                    .build();


            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .body("로그인 성공");

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



    @Transactional
    public int logout(HttpServletRequest request) {
        String token = jwtUtility.extractAccessTokenFromCookie(request);
        String email = jwtUtility.getMemberIdFromToken(token);
        Member member = memberRepository.findByMemberId(email);
        return refreshTokenRepository.deleteByMember(member);
    }
}
