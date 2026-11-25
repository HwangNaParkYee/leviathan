package com.example.leviathan.member.exception;

import com.example.leviathan.common.exception.BusinessException;
import com.example.leviathan.common.exception.ErrorCode;

public class MemberException extends BusinessException {

    public MemberException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
