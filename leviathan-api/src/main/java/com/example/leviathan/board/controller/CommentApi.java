package com.example.leviathan.board.controller;

import com.example.leviathan.board.controller.request.SaveCommentRequest;
import com.example.leviathan.board.controller.request.UpdateCommentRequest;
import com.example.leviathan.board.facade.response.FindByArticleIdCommentResponse;
import com.example.leviathan.board.facade.response.SaveCommentResponse;
import com.example.leviathan.board.facade.response.UpdateCommentResponse;
import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "댓글", description = "댓글 API")
public interface CommentApi {

	@Operation(summary = "댓글 작성")
	@ApiResponse(responseCode = "200", description = "작성 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 (B-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<SaveCommentResponse>> saveComment(
			@RequestBody SaveCommentRequest dto,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "댓글 수정", description = "작성자 본인만 수정할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "수정 성공")
	@ApiResponse(responseCode = "403", description = "작성자가 아님 (B-40304)", content = @Content)
	@ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 (B-40402)", content = @Content)
	ResponseEntity<LeviathanResponse<UpdateCommentResponse>> updateComment(
			@Parameter(description = "댓글 ID") @PathVariable Long commentId,
			@RequestBody UpdateCommentRequest dto,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "댓글 삭제", description = "작성자 본인만 삭제할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "삭제 성공")
	@ApiResponse(responseCode = "403", description = "작성자가 아님 (B-40304)", content = @Content)
	@ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 (B-40402)", content = @Content)
	ResponseEntity<LeviathanResponse<Void>> deleteComment(
			@Parameter(description = "댓글 ID") @PathVariable Long commentId,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "게시글의 댓글 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 (B-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<List<FindByArticleIdCommentResponse>>> getCommentsByArticleId(
			@Parameter(description = "게시글 ID") @PathVariable Long articleId);
}
