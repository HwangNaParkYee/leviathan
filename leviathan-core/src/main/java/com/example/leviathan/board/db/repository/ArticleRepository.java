package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Article;

import com.example.leviathan.problem.db.entity.Problem;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    Article save(Article article);

    Optional<Article> findById(Long id);

    List<Article> findAll();

    void delete(Article article);

    List<Article> findByProblemId(Problem problem);
}
