package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.Comment;
import com.example.leviathan.board.db.entity.CommentLikes;
import com.example.leviathan.member.db.entity.Member;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentLikesRepositoryImpl implements CommentLikesRepository {

    private final CommentLikesJpaRepository jpaRepository;

    @Override
    public CommentLikes save(CommentLikes commentLikes) {
        return jpaRepository.save(commentLikes);
    }

    @Override
    public void delete(CommentLikes commentLikes) {
        jpaRepository.delete(commentLikes);
    }

    @Override
    public Optional<CommentLikes> findByCommentAndMember(Comment comment, Member member) {
        return jpaRepository.findByCommentAndMember(comment, member);
    }

    @Override
    public long countByComment(Comment comment) {
        return jpaRepository.countByComment(comment);
    }
}
