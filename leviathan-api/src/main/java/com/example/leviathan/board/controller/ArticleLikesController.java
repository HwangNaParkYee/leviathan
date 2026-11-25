package com.example.leviathan.board.controller;

import com.example.leviathan.board.facade.ArticleLikesFacade;
import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ArticleLikesController implements ArticleLikesApi {

    private final ArticleLikesFacade articleLikesFacade;

    @Override
    @PostMapping("/article/likeUp/{articleId}")
    public ResponseEntity<LeviathanResponse<Void>> likeUp(@PathVariable Long articleId, @LoginId String loginId) {
        articleLikesFacade.toggleLike(loginId, articleId);
        return ResponseEntity.ok().body(LeviathanResponse.success());
    }

    @Override
    @GetMapping("article/likeCheck/{articleId}")
    public ResponseEntity<LeviathanResponse<Boolean>> findByArticleIdMemberArticleLike(@PathVariable Long articleId, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(articleLikesFacade.isLiked(loginId, articleId)));
    }
}
