package com.example.leviathan.board.controller;

import com.example.leviathan.board.dto.ArticleUpdateDTO;
import com.example.leviathan.board.dto.FindByAllArticleDTO;
import com.example.leviathan.board.dto.FindByArticleDTO;
import com.example.leviathan.board.dto.FindByProblemDTO;
import com.example.leviathan.board.dto.SaveArticleDTO;
import com.example.leviathan.board.dto.SaveResDTO;

import com.example.leviathan.board.facade.ArticleFacade;
import com.example.leviathan.global.security.LoginId;
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
    public ResponseEntity<List<FindByAllArticleDTO>> findByAll() {
        return articleFacade.findByAllArticle();
    }

    @GetMapping("/get/{articleId}")
    public ResponseEntity<FindByArticleDTO> getArticle(@PathVariable Long articleId) {
        return articleFacade.findByArticle(articleId);
    }

    @GetMapping("/findByProblemId/{problemId}")
    public ResponseEntity<List<FindByProblemDTO>> findByProblemId(@PathVariable Long problemId) {
        return articleFacade.findByProblemId(problemId);
    }

    @PostMapping("/save")
    public ResponseEntity<SaveResDTO> save(@RequestBody SaveArticleDTO dto,  @LoginId String loginId) {
        return articleFacade.saveArticle(loginId, dto);
    }

    @PutMapping("/update/{articleId}")
    public ResponseEntity<String> update(@PathVariable Long articleId, @RequestBody ArticleUpdateDTO dto, @LoginId String loginId) {
        return articleFacade.updateArticle(loginId, dto, articleId);
    }

    @DeleteMapping("/delete/{articleId}")
    public void deleteArticle(@PathVariable Long articleId,  @LoginId String loginId) {
        articleFacade.deleteArticle(loginId, articleId);
    }
}
