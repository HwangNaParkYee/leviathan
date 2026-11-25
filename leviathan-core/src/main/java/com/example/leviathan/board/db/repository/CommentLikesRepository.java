package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Comment;
import com.example.leviathan.board.db.entity.CommentLikes;

import com.example.leviathan.member.db.entity.Member;

import java.util.Optional;

public interface CommentLikesRepository {

    CommentLikes save(CommentLikes commentLikes);

    void delete(CommentLikes commentLikes);

    Optional<CommentLikes> findByCommentAndMember(Comment comment, Member member);

    long countByComment(Comment comment);
}
