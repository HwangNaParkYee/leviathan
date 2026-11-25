package com.example.leviathan.board.facade;

import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;

import com.example.leviathan.board.service.CommentLikesService;
import com.example.leviathan.board.service.CommentService;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikesFacade {

    private final CommentLikesService commentLikesService;
    private final CommentService commentService;
    private final MemberService memberService;

    @Transactional
    public boolean toggleCommentLike(Long commentId, String loginId) {
        Member member = memberService.findByLoginId(loginId);

        return commentLikesService.toggle(member, commentService.getById(commentId));
    }

    public boolean toggleLikeCheck(Long commentId, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return !commentLikesService.isLiked(member, commentService.getById(commentId));
    }

    public long getCommentLikeCount(Long commentId) {
        return commentLikesService.count(commentService.getById(commentId));
    }
}
