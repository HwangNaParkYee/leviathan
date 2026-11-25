package com.example.leviathan.common.exception;

import com.example.leviathan.board.exception.BoardErrorCode;
import com.example.leviathan.board.exception.BoardException;
import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.advice.GlobalExceptionHandler;
import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("에러 코드의 상태 코드와 메시지로 응답한다")
    void handleBusiness() {
        ResponseEntity<LeviathanResponse<Void>> notFound = handler.handleBusiness(new BoardException(BoardErrorCode.ARTICLE_NOT_FOUND));
        ResponseEntity<LeviathanResponse<Void>> forbidden = handler.handleBusiness(new BoardException(BoardErrorCode.COMMENT_ACCESS_DENIED));
        ResponseEntity<LeviathanResponse<Void>> conflict = handler.handleBusiness(new MemberException(MemberErrorCode.MEMBER_ALREADY_REGISTERED));

        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        assertEquals("B-40401", notFound.getBody().getCode());
        assertEquals("존재하지 않는 게시글입니다.", notFound.getBody().getMessage());
        assertEquals(HttpStatus.FORBIDDEN, forbidden.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, conflict.getStatusCode());
    }

    @Test
    @DisplayName("형식이 맞지 않는 에러 코드는 500으로 응답한다")
    void handleBusiness_MalformedCode() {
        ErrorCode malformed = new ErrorCode() {
            public String getCode() { return "UNKNOWN"; }
            public String getMessage() { return "알 수 없는 오류"; }
        };

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, handler.handleBusiness(new BusinessException(malformed)).getStatusCode());
    }
}
