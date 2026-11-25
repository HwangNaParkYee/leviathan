package com.example.leviathan.review.exception;

import com.example.leviathan.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    REVIEW_NOT_FOUND("R-40401", "존재하지 않는 제출 기록입니다.");

    private final String code;
    private final String message;
}
