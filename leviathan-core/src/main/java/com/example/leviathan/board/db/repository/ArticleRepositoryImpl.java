package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.problem.db.entity.Problem;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleJpaRepository jpaRepository;

    @Override
    public Article save(Article article) {
        return jpaRepository.save(article);
    }

    @Override
    public Optional<Article> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Article> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void delete(Article article) {
        jpaRepository.delete(article);
    }

    @Override
    public List<Article> findByProblemId(Problem problem) {
        return jpaRepository.findByProblemId(problem);
    }
}
