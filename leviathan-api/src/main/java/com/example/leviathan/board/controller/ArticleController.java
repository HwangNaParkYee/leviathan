package com.example.leviathan.board.controller;

import com.example.leviathan.board.controller.request.UpdateArticleRequest;
import com.example.leviathan.board.facade.response.FindByAllArticleResponse;
import com.example.leviathan.board.facade.response.FindByArticleResponse;
import com.example.leviathan.board.facade.response.FindByProblemResponse;
import com.example.leviathan.board.controller.request.SaveArticleRequest;
import com.example.leviathan.board.facade.response.SaveArticleResponse;

import com.example.leviathan.board.facade.ArticleFacade;
import com.example.leviathan.common.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleFacade articleFacade;


    @GetMapping("/findByAll")
    public ResponseEntity<List<FindByAllArticleResponse>> findByAll() {
        return articleFacade.findByAllArticle();
    }

    @GetMapping("/get/{articleId}")
    public ResponseEntity<FindByArticleResponse> getArticle(@PathVariable Long articleId) {
        return articleFacade.findByArticle(articleId);
    }

    @GetMapping("/findByProblemId/{problemId}")
    public ResponseEntity<List<FindByProblemResponse>> findByProblemId(@PathVariable Long problemId) {
        return articleFacade.findByProblemId(problemId);
    }

    @PostMapping("/save")
    public ResponseEntity<SaveArticleResponse> save(@RequestBody SaveArticleRequest dto,  @LoginId String loginId) {
        return articleFacade.saveArticle(loginId, dto);
    }

    @PutMapping("/update/{articleId}")
    public ResponseEntity<String> update(@PathVariable Long articleId, @RequestBody UpdateArticleRequest dto, @LoginId String loginId) {
        return articleFacade.updateArticle(loginId, dto, articleId);
    }

    @DeleteMapping("/delete/{articleId}")
    public void deleteArticle(@PathVariable Long articleId,  @LoginId String loginId) {
        articleFacade.deleteArticle(loginId, articleId);
    }
}
