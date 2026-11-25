package com.example.leviathan.board.db.entity;

import com.example.leviathan._common.BaseTimeEntity;
import com.example.leviathan.member.db.entity.Member;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikes extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentId")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    private CommentLikes(final Comment comment, final Member member) {
        this.comment = comment;
        this.member = member;
    }

	public static CommentLikes create(final Comment comment, final Member member) {
		return new CommentLikes(comment, member);
	}
}