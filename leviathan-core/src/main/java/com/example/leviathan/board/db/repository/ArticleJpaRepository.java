package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.problem.db.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleJpaRepository extends JpaRepository<Article, Long> {
    List<Article> findByProblemId(Problem proble);
}
