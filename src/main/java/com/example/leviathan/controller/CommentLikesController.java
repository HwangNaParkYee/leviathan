package com.example.leviathan.controller;

import com.example.leviathan.service.CommentLikesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments_likes")
@RequiredArgsConstructor
public class CommentLikesController {
    private final CommentLikesService commentLikesService;

    //댓글 좋아요 토글 API
    @PostMapping("/{commentId}")
    public ResponseEntity<Boolean> toggleCommentLike(@PathVariable Long commentId, HttpServletRequest header) {
        boolean liked = commentLikesService.toggleCommentLike(commentId, header);
        return ResponseEntity.ok(liked);
    }

    //특정 댓글의 좋아요 개수 조회 API
    @GetMapping("/{commentId}/count")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentLikesService.getCommentLikeCount(commentId));
    }
}