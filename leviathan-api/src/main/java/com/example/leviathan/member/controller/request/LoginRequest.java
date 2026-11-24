package com.example.leviathan.member.controller.request;

public record LoginRequest(
        String memberId,
        String password
) {
}
