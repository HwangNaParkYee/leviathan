package com.example.leviathan.board.db.entity;

import com.example.leviathan._common.BaseTimeEntity;
import com.example.leviathan.member.db.entity.Member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;

    private String content;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentLikes> commentLikesList = new ArrayList<>();

    private Comment(final Article article, final Member member, final String content) {
        this.articleId = article;
        this.memberId = member;
        this.content = content;
    }

	public static Comment create(final Article article, final Member member, final String content) {
		return new Comment(article, member, content);
	}

    public boolean isWrittenBy(final Member member) {
        return memberId.isSameMember(member);
    }

    public void edit(final String content) {
        this.content = content;
    }
}
