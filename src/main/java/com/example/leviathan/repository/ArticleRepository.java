package com.example.leviathan.repository;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByProblemId(Problem proble);
}
