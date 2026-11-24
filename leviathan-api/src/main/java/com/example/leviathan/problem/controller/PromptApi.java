package com.example.leviathan.problem.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.problem.controller.request.UserPromptRequest;
import com.example.leviathan.problem.facade.response.FindProblemResponse;
import com.example.leviathan.problem.model.GeneratedProblem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "AI 문제 생성", description = "프롬프트로 문제를 생성하는 API")
public interface PromptApi {

	@Operation(summary = "AI 문제 생성 및 저장", description = "프롬프트로 문제를 생성해 저장하고, 저장된 문제를 반환합니다.")
	@ApiResponse(responseCode = "200", description = "생성 성공")
	@ApiResponse(responseCode = "400", description = "리눅스 실습 문제로 부적절한 요청 (P-40004)", content = @Content)
	ResponseEntity<LeviathanResponse<FindProblemResponse>> savePromptProblem(
			@RequestBody UserPromptRequest dto,
			@Parameter(hidden = true) @LoginId String loginId);

	@Operation(summary = "AI 문제 미리보기", description = "저장하지 않고 생성 결과만 반환합니다.")
	@ApiResponse(responseCode = "200", description = "생성 성공")
	ResponseEntity<LeviathanResponse<GeneratedProblem>> generate(@RequestBody UserPromptRequest dto);
}
