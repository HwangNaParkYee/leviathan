package com.example.leviathan.board.exception;

import com.example.leviathan.common.exception.BusinessException;
import com.example.leviathan.common.exception.ErrorCode;

public class BoardException extends BusinessException {

    public BoardException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
