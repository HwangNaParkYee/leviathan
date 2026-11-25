package com.example.leviathan.board.controller.request;

public record UpdateArticleRequest(
        String title,
        String content,
        String code
) {
}
