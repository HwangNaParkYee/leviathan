package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Comment;
import com.example.leviathan.board.db.entity.CommentLikes;
import com.example.leviathan.member.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikesJpaRepository extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByCommentAndMember(Comment comment, Member member);

    long countByComment(Comment comment);

}
