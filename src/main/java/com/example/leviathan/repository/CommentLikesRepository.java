package com.example.leviathan.repository;

import com.example.leviathan.domain.Comment;
import com.example.leviathan.domain.CommentLikes;
import com.example.leviathan.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByCommentAndMember(Comment comment, Member member);

    long countByComment(Comment comment);

}
