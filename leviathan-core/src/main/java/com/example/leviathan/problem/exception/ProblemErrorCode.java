package com.example.leviathan.problem.exception;

import com.example.leviathan.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProblemErrorCode implements ErrorCode {

    PROBLEM_NOT_FOUND("P-40401", "존재하지 않는 문제입니다."),
    DUPLICATED_PROBLEM_NUMBER("P-40902", "이미 존재하는 문제 번호입니다."),
    PROBLEM_EXECUTION_FAILED("P-50003", "문제 실행 중 오류가 발생했습니다.");

    private final String code;
    private final String message;
}
