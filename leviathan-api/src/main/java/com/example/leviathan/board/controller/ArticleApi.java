package com.example.leviathan.board.controller;

import com.example.leviathan.board.controller.request.SaveArticleRequest;
import com.example.leviathan.board.controller.request.UpdateArticleRequest;
import com.example.leviathan.board.facade.response.FindByAllArticleResponse;
import com.example.leviathan.board.facade.response.FindByArticleResponse;
import com.example.leviathan.board.facade.response.FindByProblemResponse;
import com.example.leviathan.board.facade.response.SaveArticleResponse;
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

@Tag(name = "게시글", description = "게시글 API")
public interface ArticleApi {

	@Operation(summary = "게시글 전체 조회", description = "모든 게시글을 좋아요 수, 댓글 수와 함께 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<List<FindByAllArticleResponse>>> findByAll();

	@Operation(summary = "게시글 단건 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 (B-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<FindByArticleResponse>> getArticle(
			@Parameter(description = "게시글 ID") @PathVariable Long articleId);

	@Operation(summary = "문제별 게시글 조회", description = "문제 ID에 달린 게시글을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<List<FindByProblemResponse>>> findByProblemId(
			@Parameter(description = "문제 ID") @PathVariable Long problemId);

	@Operation(summary = "게시글 작성")
	@ApiResponse(responseCode = "200", description = "작성 성공")
	ResponseEntity<LeviathanResponse<SaveArticleResponse>> save(
			@RequestBody SaveArticleRequest dto,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "게시글 수정", description = "작성자 본인만 수정할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "수정 성공")
	@ApiResponse(responseCode = "403", description = "작성자가 아님 (B-40303)", content = @Content)
	@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 (B-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<String>> update(
			@Parameter(description = "게시글 ID") @PathVariable Long articleId,
			@RequestBody UpdateArticleRequest dto,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "게시글 삭제", description = "작성자 본인만 삭제할 수 있습니다.")
	@ApiResponse(responseCode = "200", description = "삭제 성공")
	@ApiResponse(responseCode = "403", description = "작성자가 아님 (B-40303)", content = @Content)
	@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 (B-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<Void>> deleteArticle(
			@Parameter(description = "게시글 ID") @PathVariable Long articleId,
			@Parameter(hidden = true) @LoginId String loginId);
}
