package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.db.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    Long countByArticleId(Article articleId);

    List<Comment> findByArticleId(Article article);

}
