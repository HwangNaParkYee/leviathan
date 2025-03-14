package com.example.leviathan.repository;

import com.example.leviathan.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByArticleId(Long articleId);
}
