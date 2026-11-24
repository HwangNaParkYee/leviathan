package com.example.leviathan.member.facade;

import com.example.leviathan.member.exception.MemberException;

import com.example.leviathan.global.security.AccessTokenCookie;
import com.example.leviathan.global.security.JwtUtility;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.dto.LoginDTO;
import com.example.leviathan.member.dto.SignUpDTO;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.member.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFacade {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

    // 서비스가 던진 예외를 여기서 응답으로 바꾸므로 Facade에서는 트랜잭션을 열지 않는다
    // (트랜잭션을 열면 rollback-only로 표시되어 응답 대신 500이 나간다)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResponseEntity<String> signUp(SignUpDTO dto) {
        try {
            // 비밀번호 암호화 후 회원 저장
            memberService.register(dto.getMemberId(), passwordEncoder.encode(dto.getPassword()));
            return ResponseEntity.ok("회원가입 성공");
        } catch (MemberException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("이미 사용 중인 회원 ID입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public ResponseEntity<Object> login(LoginDTO dto) {
        // 사용자 ID로 회원 정보를 찾음
        Member member = memberService.findByLoginId(dto.getMemberId());

        // 회원이 존재하고, 비밀번호가 일치하는지 확인
        if (member != null && passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            // 비밀번호가 맞다면 JWT 토큰을 생성
            String accessToken = jwtUtility.generateToken(dto.getMemberId(), member.getRole());
            String refreshToken = jwtUtility.generateRefreshToken(dto.getMemberId());

            refreshTokenService.issue(member, refreshToken, LocalDateTime.now().plus(JwtUtility.REFRESH_TOKEN_VALIDITY));

            ResponseCookie accessTokenCookie = AccessTokenCookie.of(accessToken);

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
    public ResponseEntity<String> deleteMember(Long memberId) {
        if (!memberService.delete(memberId)) {
            // 멤버가 존재하지 않으면 404 오류 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }
        return ResponseEntity.ok("Member deleted successfully");
    }

    @Transactional
    public int logout(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return refreshTokenService.revoke(member);
    }
}
