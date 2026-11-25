package com.example.leviathan.member.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.member.controller.request.LoginRequest;
import com.example.leviathan.member.controller.request.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "회원", description = "회원 API")
public interface MemberApi {

	@Operation(summary = "회원가입")
	@ApiResponse(responseCode = "200", description = "가입 성공")
	@ApiResponse(responseCode = "409", description = "이미 사용 중인 회원 ID (M-40902)", content = @Content)
	ResponseEntity<LeviathanResponse<Void>> signUp(@RequestBody SignUpRequest dto);

	@Operation(summary = "회원 삭제")
	@ApiResponse(responseCode = "200", description = "삭제 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 회원 (M-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<Void>> deleteMember(
			@Parameter(description = "회원 ID") @PathVariable Long memberId);

	@Operation(summary = "로그인", description = "성공하면 accessToken 쿠키를 내려줍니다.")
	@ApiResponse(responseCode = "200", description = "로그인 성공")
	@ApiResponse(responseCode = "400", description = "아이디 또는 비밀번호 불일치 (M-40003)", content = @Content)
	ResponseEntity<LeviathanResponse<Void>> login(@RequestBody LoginRequest dto);

	@Operation(summary = "로그아웃", description = "RefreshToken을 폐기하고 accessToken 쿠키를 만료시킵니다.")
	@ApiResponse(responseCode = "200", description = "로그아웃 성공")
	ResponseEntity<LeviathanResponse<Void>> logout(@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "로그인 상태 확인")
	@ApiResponse(responseCode = "200", description = "로그인 상태")
	@ApiResponse(responseCode = "401", description = "로그인하지 않음", content = @Content)
	ResponseEntity<LeviathanResponse<Map<String, Object>>> getCurrentUser(
			@Parameter(hidden = true) Authentication authentication);
}
