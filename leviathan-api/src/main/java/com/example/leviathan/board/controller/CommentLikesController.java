package com.example.leviathan.board.controller;

import com.example.leviathan.board.facade.CommentLikesFacade;
import com.example.leviathan.global.security.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments_likes")
@RequiredArgsConstructor
public class CommentLikesController {
    private final CommentLikesFacade commentLikesFacade;

    //댓글 좋아요 토글 API
    @PostMapping("/{commentId}")
    public ResponseEntity<Boolean> toggleCommentLike(@PathVariable Long commentId, @LoginId String loginId) {
        boolean liked = commentLikesFacade.toggleCommentLike(commentId, loginId);
        return ResponseEntity.ok(liked);
    }

    //특정 댓글의 좋아요 개수 조회 API
    @GetMapping("/count/{commentId}")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentLikesFacade.getCommentLikeCount(commentId));
    }

    //좋아요 했는지 체크용
    @GetMapping("/likeCheck/{commentId}")
    public ResponseEntity<Boolean> toggleLikeCheck(@PathVariable Long commentId, @LoginId String loginId) {
        boolean likeCheck = commentLikesFacade.toggleLikeCheck(commentId, loginId);
        return ResponseEntity.ok(likeCheck);
    }
}