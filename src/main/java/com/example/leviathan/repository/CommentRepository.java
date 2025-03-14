package com.example.leviathan.repository;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByArticleId(Long articleId);

    List<Comment> findByArticleId(Article article);

}
