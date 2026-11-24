package com.example.leviathan.board.controller.request;

public record SaveCommentRequest(
        Long articleId,
        String content
) {
}
