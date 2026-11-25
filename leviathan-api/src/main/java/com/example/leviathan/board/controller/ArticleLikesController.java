package com.example.leviathan.board.controller;

import com.example.leviathan.board.dto.ArticleLikesDTO;
import com.example.leviathan.board.facade.ArticleLikesFacade;
import com.example.leviathan.common.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleLikesController {
    private final ArticleLikesFacade articleLikesFacade;

    @PostMapping("/article/likeUp/{articleId}")
    public ResponseEntity<String> likeUp(@PathVariable Long articleId, @LoginId String loginId) {
        return articleLikesFacade.toggleLike(loginId, articleId);
    }

    @GetMapping("article/likeCheck/{articleId}")
    public ResponseEntity<String> findByArticleIdMemberArticleLike(@PathVariable Long articleId, @LoginId String loginId){
        return articleLikesFacade.findByArticleIdMemberArticleLike(loginId, articleId);
    }
}
