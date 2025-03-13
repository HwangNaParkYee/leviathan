package com.example.leviathan.controller;

import com.example.leviathan.dto.*;
import com.example.leviathan.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/article/findByAll")
    public ResponseEntity<List<FindByAllArticleDTO>> findByAll() {
        return articleService.findByAllArticle();
    }

    @GetMapping("/article/get/{articleId}")
    public ResponseEntity<FindByArticleDTO> getArticle(@PathVariable Long articleId) {
        return articleService.findByArticle(articleId);
    }

    @GetMapping("/article/findByProblemId/{problemId}")
    public ResponseEntity<List<FindByProblemDTO>> findByProblemId(@PathVariable Long problemId) {
        return articleService.findByProblemId(problemId);
    }

    @PostMapping("/article/save")
    public ResponseEntity<SaveResDTO> save(@RequestBody SaveArticleDTO dto, String token) {
        return articleService.saveArticle(dto, token);
    }

    @PutMapping("/article/update/{articleId}")
    public ResponseEntity<String> update(@PathVariable Long articleId, @RequestBody ArticleUpdateDTO dto, String token) {
        return articleService.updateArticle(dto, token, articleId);
    }

    @DeleteMapping("/article/delete/{articleId}")
    public void deleteArticle(@PathVariable Long articleId, String token) {
        articleService.deleteArticle(articleId, token);
    }
}
