package com.example.leviathan.controller;

import com.example.leviathan.dto.ArticleLikesDTO;
import com.example.leviathan.service.ArticleLikesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleLikesController {
    private final ArticleLikesService articleLikesService;

    @PostMapping("/article/likeUp/{articleId}")
    public ResponseEntity<String> likeUp(@PathVariable Long articleId, HttpServletRequest header) {
        return articleLikesService.toggleLike(header, articleId);
    }

    @GetMapping("article/likeCheck/{articleId}")
    public ResponseEntity<String> findByArticleIdMemberArticleLike(@PathVariable Long articleId, HttpServletRequest header){
        return articleLikesService.findByArticleIdMemberArticleLike(header, articleId);
    }
}
