package com.example.leviathan.board.controller.request;

public record SaveArticleRequest(
        Long problemId,
        String title,
        String content,
        String code
) {
}
