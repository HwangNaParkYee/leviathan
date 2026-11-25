package com.example.leviathan.board.service;

import com.example.leviathan.board.db.entity.Comment;
import com.example.leviathan.board.db.entity.CommentLikes;
import com.example.leviathan.board.db.repository.CommentLikesRepository;
import com.example.leviathan.member.db.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikesService {

    private final CommentLikesRepository commentLikesRepository;

    @Transactional
    public boolean toggle(Member member, Comment comment) {
        return commentLikesRepository.findByCommentAndMember(comment, member)
                .map(like -> {
                    // 좋아요가 이미 존재하면 삭제
                    commentLikesRepository.delete(like);
                    return false;
                })
                .orElseGet(() -> {
                    // 좋아요가 없으면 추가
                    commentLikesRepository.save(new CommentLikes(comment, member));
                    return true;
                });
    }

    public boolean isLiked(Member member, Comment comment) {
        return commentLikesRepository.findByCommentAndMember(comment, member).isPresent();
    }

    //좋아요 갯수 카운트
    public long count(Comment comment) {
        return commentLikesRepository.countByComment(comment);
    }
}
