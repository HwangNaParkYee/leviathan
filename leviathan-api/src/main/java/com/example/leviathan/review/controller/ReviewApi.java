package com.example.leviathan.review.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.review.facade.response.ActivityHistoryResponse;
import com.example.leviathan.review.facade.response.MyPageStatisticsResponse;
import com.example.leviathan.review.facade.response.ProblemStatisticsResponse;
import com.example.leviathan.review.facade.response.ReviewResponse;
import com.example.leviathan.review.facade.response.SolvedProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "제출 기록", description = "제출 기록 및 통계 API")
public interface ReviewApi {

	@Operation(summary = "제출 기록 전체 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<List<ReviewResponse>>> getAllReviews();

	@Operation(summary = "문제별 제출 기록 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<List<ReviewResponse>>> getReviewsByProblemId(
			@Parameter(description = "문제 ID") @PathVariable Long problemId);

	@Operation(summary = "문제별 내 제출 기록 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<List<ReviewResponse>>> getReviewsByProblemIdAndMemberId(
			@Parameter(description = "문제 ID") @PathVariable Long problemId,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "내 제출 기록 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<List<ReviewResponse>>> getReviewsByMemberId(
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "제출 기록 단건 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 제출 기록 (R-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<ReviewResponse>> getReviewById(
			@Parameter(description = "제출 기록 ID") @PathVariable Long detailId);

	@Operation(summary = "내가 맞힌 문제 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<List<SolvedProblemResponse>>> getSolvedProblems(
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "내가 틀린 문제 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<List<SolvedProblemResponse>>> getWrongProblems(
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "날짜별 활동 기록 조회", description = "날짜별 제출 횟수를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<List<ActivityHistoryResponse>>> getActivityHistory(
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "마이페이지 통계 조회", description = "푼 문제 수, 도전한 문제 수, 제출 횟수를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<MyPageStatisticsResponse>> getStatistics(
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "문제별 통계 조회", description = "총 제출 횟수, 정답 제출 횟수, 맞힌 사람 수를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<ProblemStatisticsResponse>> getProblemStatistics(
			@Parameter(description = "문제 ID") @PathVariable Long problemId);
}
