package com.example.leviathan.controller;

import com.example.leviathan.service.CommentLikesService;
import com.example.leviathan.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments_likes")
@RequiredArgsConstructor
public class CommentLikesController {
    private final CommentLikesService commentLikesService;
    private final CommentService commentService;

    //댓글 좋아요 토글 API
    @PostMapping("/{commentId}")
    public ResponseEntity<Boolean> toggleCommentLike(@PathVariable Long commentId, HttpServletRequest header) {
        boolean liked = commentLikesService.toggleCommentLike(commentId, header);
        return ResponseEntity.ok(liked);
    }

    //특정 댓글의 좋아요 개수 조회 API
    @GetMapping("/count/{commentId}")
    public ResponseEntity<Long> getCommentLikeCount(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentLikesService.getCommentLikeCount(commentId));
    }

    //좋아요 했는지 체크용
    @GetMapping("/likeCheck/{commentId}")
    public ResponseEntity<Boolean> toggleLikeCheck(@PathVariable Long commentId, HttpServletRequest header) {
        boolean likeCheck = commentLikesService.toggleLikeCheck(commentId, header);
        return ResponseEntity.ok(likeCheck);
    }
}