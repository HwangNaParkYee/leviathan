package com.example.leviathan.problem.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.problem.facade.response.ProblemLikeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "문제 좋아요", description = "문제 좋아요 API")
public interface ProblemLikesApi {

	@Operation(summary = "문제 좋아요 토글", description = "좋아요가 없으면 추가하고, 있으면 취소합니다.")
	@ApiResponse(responseCode = "200", description = "토글 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<String>> toggleProblemLike(
			@Parameter(description = "문제 ID") @PathVariable Long problemId,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "문제 좋아요 수 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<Long>> getProblemLikeCount(
			@Parameter(description = "문제 ID") @PathVariable Long problemId);

	@Operation(summary = "문제 좋아요 여부 확인", description = "로그인한 회원이 좋아요를 눌렀으면 true를 반환합니다.")
	@ApiResponse(responseCode = "200", description = "확인 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 회원 (M-40401) 또는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<Boolean>> checkProblemLike(
			@Parameter(description = "문제 ID") @PathVariable Long problemId,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "내가 좋아요한 문제 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 회원 (M-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<List<ProblemLikeResponse>>> getUserLikedProblems(
			@Parameter(hidden = true) @LoginId String loginId);
}
