package com.example.leviathan.controller;


import com.example.leviathan.domain.Comment;
import com.example.leviathan.dto.*;
import com.example.leviathan.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 달기
    @PostMapping("/save")
    public ResponseEntity<CommentDTO> saveComment(
            @RequestBody SaveCommentDTO dto, HttpServletRequest header) {
        return ResponseEntity.ok(commentService.saveComment(dto, header));
    }

    //댓글 수정
    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentDTO dto, HttpServletRequest header) {
        return ResponseEntity.ok(commentService.updateComment(commentId, dto, header));
    }

    //댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, HttpServletRequest header) {
        commentService.deleteComment(commentId, header);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    //게시글 댓글 조회
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<FindByArticleIdCommentDTO>> getCommentsByArticleId(@PathVariable Long articleId) {
        return ResponseEntity.ok(commentService.getCommentsByArticleId(articleId));
    }
}
