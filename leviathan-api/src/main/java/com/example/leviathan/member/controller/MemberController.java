package com.example.leviathan.member.controller;

import com.example.leviathan.common.AccessTokenCookie;
import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.member.controller.request.LoginRequest;
import com.example.leviathan.member.controller.request.SignUpRequest;
import com.example.leviathan.member.facade.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController implements MemberApi {

    private final MemberFacade memberFacade;

    @Override
    @PostMapping("/signUp")
    public ResponseEntity<LeviathanResponse<Void>> signUp(@RequestBody SignUpRequest dto) {
        memberFacade.signUp(dto);
        return ResponseEntity.ok().body(LeviathanResponse.success());
    }

    @Override
    @DeleteMapping("/deleteMember/{memberId}")
    public ResponseEntity<LeviathanResponse<Void>> deleteMember(@PathVariable Long memberId) {
        memberFacade.deleteMember(memberId);
        return ResponseEntity.ok().body(LeviathanResponse.success());
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LeviathanResponse<Void>> login(@RequestBody LoginRequest dto) {
        String accessToken = memberFacade.login(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, AccessTokenCookie.of(accessToken).toString())
                .body(LeviathanResponse.success());
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<LeviathanResponse<Void>> logout(@LoginId String loginId) {
        memberFacade.logout(loginId);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, AccessTokenCookie.expired().toString())
                .body(LeviathanResponse.success());
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<LeviathanResponse<Map<String, Object>>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LeviathanResponse.fail("UNAUTHORIZED", "로그인이 필요합니다.", Map.of("isAuthenticated", false)));
        }

        return ResponseEntity.ok().body(LeviathanResponse.success(Map.of(
                "isAuthenticated", true,
                "username", authentication.getName()
        )));
    }
}
