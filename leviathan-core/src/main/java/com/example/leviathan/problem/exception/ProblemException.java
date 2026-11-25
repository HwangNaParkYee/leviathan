package com.example.leviathan.problem.exception;

import com.example.leviathan.common.exception.BusinessException;
import com.example.leviathan.common.exception.ErrorCode;

public class ProblemException extends BusinessException {

    public ProblemException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public ProblemException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }
}
