package com.example.leviathan.member.exception;

import com.example.leviathan.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND("M-40401", "존재하지 않는 회원입니다."),
    MEMBER_ALREADY_REGISTERED("M-40902", "이미 사용 중인 회원 ID입니다.");

    private final String code;
    private final String message;
}
