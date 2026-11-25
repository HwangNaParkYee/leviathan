package com.example.leviathan.board.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "댓글 좋아요", description = "댓글 좋아요 API")
public interface CommentLikesApi {

	@Operation(summary = "댓글 좋아요 토글", description = "좋아요를 추가했으면 true, 취소했으면 false를 반환합니다.")
	@ApiResponse(responseCode = "200", description = "토글 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 (B-40402)", content = @Content)
	ResponseEntity<LeviathanResponse<Boolean>> toggleCommentLike(
			@Parameter(description = "댓글 ID") @PathVariable Long commentId,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "댓글 좋아요 수 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 (B-40402)", content = @Content)
	ResponseEntity<LeviathanResponse<Long>> getCommentLikeCount(
			@Parameter(description = "댓글 ID") @PathVariable Long commentId);

	@Operation(summary = "댓글 좋아요 여부 확인", description = "좋아요를 누르지 않았으면 true, 눌렀으면 false를 반환합니다.")
	@ApiResponse(responseCode = "200", description = "확인 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 회원 (M-40401) 또는 댓글 (B-40402)", content = @Content)
	ResponseEntity<LeviathanResponse<Boolean>> toggleLikeCheck(
			@Parameter(description = "댓글 ID") @PathVariable Long commentId,
			@Parameter(hidden = true) @LoginId String loginId);
}
