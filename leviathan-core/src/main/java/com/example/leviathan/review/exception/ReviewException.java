package com.example.leviathan.review.exception;

import com.example.leviathan.common.exception.BusinessException;
import com.example.leviathan.common.exception.ErrorCode;

public class ReviewException extends BusinessException {

    public ReviewException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
