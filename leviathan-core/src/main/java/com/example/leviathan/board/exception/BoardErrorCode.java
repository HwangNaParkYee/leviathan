package com.example.leviathan.board.exception;

import com.example.leviathan.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {

    ARTICLE_NOT_FOUND("B-40401", "존재하지 않는 게시글입니다."),
    COMMENT_NOT_FOUND("B-40402", "존재하지 않는 댓글입니다."),
    ARTICLE_ACCESS_DENIED("B-40303", "본인이 작성한 게시글만 수정하거나 삭제할 수 있습니다."),
    COMMENT_ACCESS_DENIED("B-40304", "본인이 작성한 댓글만 수정하거나 삭제할 수 있습니다.");

    private final String code;
    private final String message;
}
