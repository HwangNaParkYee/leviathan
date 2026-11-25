package com.example.leviathan.board.controller;

import com.example.leviathan.board.controller.request.SaveCommentRequest;
import com.example.leviathan.board.controller.request.UpdateCommentRequest;
import com.example.leviathan.board.facade.CommentFacade;
import com.example.leviathan.board.facade.response.FindByArticleIdCommentResponse;
import com.example.leviathan.board.facade.response.SaveCommentResponse;
import com.example.leviathan.board.facade.response.UpdateCommentResponse;
import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController implements CommentApi {

    private final CommentFacade commentFacade;

    @Override
    @PostMapping("/save")
    public ResponseEntity<LeviathanResponse<SaveCommentResponse>> saveComment(@RequestBody SaveCommentRequest dto, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(commentFacade.saveComment(dto, loginId)));
    }

    @Override
    @PutMapping("/update/{commentId}")
    public ResponseEntity<LeviathanResponse<UpdateCommentResponse>> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest dto, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(commentFacade.updateComment(commentId, dto, loginId)));
    }

    @Override
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<LeviathanResponse<Void>> deleteComment(@PathVariable Long commentId, @LoginId String loginId) {
        commentFacade.deleteComment(commentId, loginId);
        return ResponseEntity.ok().body(LeviathanResponse.success());
    }

    @Override
    @GetMapping("/article/{articleId}")
    public ResponseEntity<LeviathanResponse<List<FindByArticleIdCommentResponse>>> getCommentsByArticleId(@PathVariable Long articleId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(commentFacade.getCommentsByArticleId(articleId)));
    }
}
