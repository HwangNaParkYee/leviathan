package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.db.entity.Comment;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository jpaRepository;

    @Override
    public Comment save(Comment comment) {
        return jpaRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void delete(Comment comment) {
        jpaRepository.delete(comment);
    }

    @Override
    public Long countByArticleId(Article articleId) {
        return jpaRepository.countByArticleId(articleId);
    }

    @Override
    public List<Comment> findByArticleId(Article article) {
        return jpaRepository.findByArticleId(article);
    }
}
