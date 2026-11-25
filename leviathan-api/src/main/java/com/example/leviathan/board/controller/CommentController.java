package com.example.leviathan.board.controller;

import com.example.leviathan.board.facade.response.SaveCommentResponse;
import com.example.leviathan.board.facade.response.UpdateCommentResponse;
import com.example.leviathan.board.facade.response.FindByArticleIdCommentResponse;
import com.example.leviathan.board.controller.request.SaveCommentRequest;
import com.example.leviathan.board.controller.request.UpdateCommentRequest;


import com.example.leviathan.board.facade.CommentFacade;
import com.example.leviathan.common.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentFacade commentFacade;

    //댓글 달기
    @PostMapping("/save")
    public ResponseEntity<SaveCommentResponse> saveComment( @RequestBody SaveCommentRequest dto, @LoginId String loginId) {
        return ResponseEntity.ok(commentFacade.saveComment(dto, loginId));
    }

    //댓글 수정
    @PutMapping("/update/{commentId}")
    public ResponseEntity<UpdateCommentResponse> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest dto, @LoginId String loginId) {
        return ResponseEntity.ok(commentFacade.updateComment(commentId, dto, loginId));
    }

    //댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @LoginId String loginId) {
        commentFacade.deleteComment(commentId, loginId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    //게시글 댓글 조회
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<FindByArticleIdCommentResponse>> getCommentsByArticleId(@PathVariable Long articleId) {
        return ResponseEntity.ok(commentFacade.getCommentsByArticleId(articleId));
    }
}
