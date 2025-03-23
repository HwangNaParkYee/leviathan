package com.example.leviathan.service;

import com.example.leviathan.domain.Comment;
import com.example.leviathan.domain.CommentLikes;
import com.example.leviathan.domain.Member;
import com.example.leviathan.repository.CommentLikesRepository;
import com.example.leviathan.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikesService {
    private final CommentLikesRepository commentLikesRepository;
    private final CommentRepository commentRepository;
    private final MemberService memberService;

    //댓글 좋아요 토글 형식
    public boolean toggleCommentLike(Long commentId, HttpServletRequest header) {
        Member member = memberService.tokenToMember(header); // JWT에서 사용자 정보 추출

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. ID: " + commentId));

        //있는지 확인
        return commentLikesRepository.findByCommentAndMember(comment, member)
                .map(like -> {
                    // 좋아요가 이미 존재하면 삭제
                    commentLikesRepository.delete(like);
                    return false; // 좋아요 취소됨
                })
                .orElseGet(() -> {
                    // 좋아요가 없으면 추가
                    commentLikesRepository.save(new CommentLikes(comment, member));
                    return true; // 좋아요 추가됨
                });
    }

    public boolean toggleLikeCheck(Long commentId, HttpServletRequest header) {
        Member member;
        try {
            member = memberService.tokenToMember(header); // JWT에서 사용자 정보 추출
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보를 가져올 수 없습니다. (토큰 확인 필요)", e);
        }

        if (member == null) {
            throw new RuntimeException("로그인한 사용자만 좋아요 가능.");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. ID: " + commentId));

        // 좋아요 없으면 true, 좋아요 있으면 false
        return commentLikesRepository.findByCommentAndMember(comment, member).isEmpty();
    }


    //좋아요 갯수 카운트
    public long getCommentLikeCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. ID: " + commentId));

        return commentLikesRepository.countByComment(comment);
    }
}