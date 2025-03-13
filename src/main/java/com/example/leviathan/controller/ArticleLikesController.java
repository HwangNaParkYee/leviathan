package com.example.leviathan.controller;

import com.example.leviathan.dto.ArticleLikesDTO;
import com.example.leviathan.service.ArticleLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleLikesController {
    private final ArticleLikesService articleLikesService;

    @PostMapping("/article/likeUp/{articleId}")
    public ResponseEntity<ArticleLikesDTO> likeUp(@PathVariable Long articleId, String token) {
        return articleLikesService.toggleLike(articleId, token);
    }
}
