package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.db.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    void delete(Comment comment);

    Long countByArticleId(Article articleId);

    List<Comment> findByArticleId(Article article);
}
