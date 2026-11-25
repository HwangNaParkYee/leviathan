package com.example.leviathan.board.controller;

import com.example.leviathan.board.controller.request.SaveArticleRequest;
import com.example.leviathan.board.controller.request.UpdateArticleRequest;
import com.example.leviathan.board.facade.ArticleFacade;
import com.example.leviathan.board.facade.response.FindByAllArticleResponse;
import com.example.leviathan.board.facade.response.FindByArticleResponse;
import com.example.leviathan.board.facade.response.FindByProblemResponse;
import com.example.leviathan.board.facade.response.SaveArticleResponse;
import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleFacade articleFacade;
    @GetMapping("/findByAll")
    public ResponseEntity<LeviathanResponse<List<FindByAllArticleResponse>>> findByAll() {
        return ResponseEntity.ok().body(LeviathanResponse.success(articleFacade.findByAllArticle()));
    }
    @GetMapping("/get/{articleId}")
    public ResponseEntity<LeviathanResponse<FindByArticleResponse>> getArticle(@PathVariable Long articleId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(articleFacade.findByArticle(articleId)));
    }
    @GetMapping("/findByProblemId/{problemId}")
    public ResponseEntity<LeviathanResponse<List<FindByProblemResponse>>> findByProblemId(@PathVariable Long problemId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(articleFacade.findByProblemId(problemId)));
    }
    @PostMapping("/save")
    public ResponseEntity<LeviathanResponse<SaveArticleResponse>> save(@RequestBody SaveArticleRequest dto, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(articleFacade.saveArticle(loginId, dto)));
    }
    @PutMapping("/update/{articleId}")
    public ResponseEntity<LeviathanResponse<String>> update(@PathVariable Long articleId, @RequestBody UpdateArticleRequest dto, @LoginId String loginId) {
        articleFacade.updateArticle(loginId, dto, articleId);
        return ResponseEntity.ok().body(LeviathanResponse.success("게시물이 수정되었습니다."));
    }
    @DeleteMapping("/delete/{articleId}")
    public ResponseEntity<LeviathanResponse<Void>> deleteArticle(@PathVariable Long articleId, @LoginId String loginId) {
        articleFacade.deleteArticle(loginId, articleId);
        return ResponseEntity.ok().body(LeviathanResponse.success());
    }
}
