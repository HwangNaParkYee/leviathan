package com.example.leviathan.board.controller;

import com.example.leviathan.board.facade.CommentLikesFacade;
import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments_likes")
@RequiredArgsConstructor
public class CommentLikesController implements CommentLikesApi {

    private final CommentLikesFacade commentLikesFacade;

    @Override
    @PostMapping("/{commentId}")
    public ResponseEntity<LeviathanResponse<Boolean>> toggleCommentLike(@PathVariable Long commentId, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(commentLikesFacade.toggleCommentLike(commentId, loginId)));
    }

    @Override
    @GetMapping("/count/{commentId}")
    public ResponseEntity<LeviathanResponse<Long>> getCommentLikeCount(@PathVariable Long commentId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(commentLikesFacade.getCommentLikeCount(commentId)));
    }

    @Override
    @GetMapping("/likeCheck/{commentId}")
    public ResponseEntity<LeviathanResponse<Boolean>> toggleLikeCheck(@PathVariable Long commentId, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(commentLikesFacade.toggleLikeCheck(commentId, loginId)));
    }
}
