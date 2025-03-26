package com.example.leviathan.controller;

import com.example.leviathan.dto.*;
import com.example.leviathan.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/findByAll")
    public ResponseEntity<List<FindByAllArticleDTO>> findByAll() {
        return articleService.findByAllArticle();
    }

    @GetMapping("/get/{articleId}")
    public ResponseEntity<FindByArticleDTO> getArticle(@PathVariable Long articleId) {
        return articleService.findByArticle(articleId);
    }

    @GetMapping("/findByProblemId/{problemId}")
    public ResponseEntity<List<FindByProblemDTO>> findByProblemId(@PathVariable Long problemId) {
        return articleService.findByProblemId(problemId);
    }

    @PostMapping("/save")
    public ResponseEntity<SaveResDTO> save(@RequestBody SaveArticleDTO dto,  HttpServletRequest header) {
        return articleService.saveArticle(header, dto);
    }

    @PutMapping("/update/{articleId}")
    public ResponseEntity<String> update(@PathVariable Long articleId, @RequestBody ArticleUpdateDTO dto, HttpServletRequest header) {
        return articleService.updateArticle(header, dto, articleId);
    }

    @DeleteMapping("/delete/{articleId}")
    public void deleteArticle(@PathVariable Long articleId,  HttpServletRequest header) {
        articleService.deleteArticle(header, articleId);
    }
}
