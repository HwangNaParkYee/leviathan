package com.example.leviathan.problem.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.problem.controller.request.SaveProblemRequest;
import com.example.leviathan.problem.controller.request.SubmitProblemRequest;
import com.example.leviathan.problem.controller.request.UpdateProblemRequest;
import com.example.leviathan.problem.facade.response.FindByAllProblemResponse;
import com.example.leviathan.problem.facade.response.FindProblemResponse;
import com.example.leviathan.problem.facade.response.MyProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "문제", description = "문제 API")
public interface ProblemApi {

	@Operation(summary = "문제 등록")
	@ApiResponse(responseCode = "200", description = "등록 성공")
	@ApiResponse(responseCode = "409", description = "이미 존재하는 문제 번호 (P-40902)", content = @Content)
	ResponseEntity<LeviathanResponse<Void>> saveProblem(@RequestBody SaveProblemRequest dto);

	@Operation(summary = "문제 단건 조회", description = "문제 ID로 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<FindProblemResponse>> findProblem(
			@Parameter(description = "문제 ID") @PathVariable Long problemId);

	@Operation(summary = "문제 번호로 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<FindProblemResponse>> findByProblemNumber(
			@Parameter(description = "문제 번호") @PathVariable Long problemNumber);

	@Operation(summary = "문제 전체 조회", description = "모든 문제를 좋아요 수, 정답률과 함께 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<List<FindByAllProblemResponse>>> findByAllProblem();

	@Operation(summary = "문제 수정")
	@ApiResponse(responseCode = "200", description = "수정 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 문제 (P-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<Void>> updateProblem(@RequestBody UpdateProblemRequest dto);

	@Operation(summary = "명령어 실행", description = "채점하지 않고 명령어 실행 결과만 반환합니다.")
	@ApiResponse(responseCode = "200", description = "실행 결과")
	ResponseEntity<LeviathanResponse<String>> runProblem(@RequestBody SubmitProblemRequest dto);

	@Operation(summary = "답안 제출", description = "명령어를 채점하고 제출 기록을 남깁니다.")
	@ApiResponse(responseCode = "200", description = "채점 결과")
	ResponseEntity<LeviathanResponse<String>> submitProblem(
			@RequestBody SubmitProblemRequest dto,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "문제 번호 전체 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<LeviathanResponse<List<Long>>> getAllProblemNumbers();

	@Operation(summary = "내가 만든 문제 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@ApiResponse(responseCode = "404", description = "존재하지 않는 회원 (M-40401)", content = @Content)
	ResponseEntity<LeviathanResponse<List<MyProblemResponse>>> getAllMyProblemNumbers(
			@Parameter(hidden = true) @LoginId String loginId);
}
