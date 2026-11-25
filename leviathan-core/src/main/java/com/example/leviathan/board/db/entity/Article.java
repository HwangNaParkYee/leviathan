package com.example.leviathan.board.db.entity;

import com.example.leviathan._common.BaseTimeEntity;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problemId;

	@Column(nullable = false)
    private String title;

	@Column(nullable = false)
    private String content;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member memberId;

    private Article(final Problem problemId, final String title, final String content, final String code, final Member memberId) {
        this.problemId = problemId;
        this.title = title;
        this.content = content;
        this.code = code;
        this.memberId = memberId;
    }

	public static Article create(final Problem problemId, final String title, final String content, final String code, final Member memberId) {
		return new Article(problemId, title, content, code, memberId);
	}

    public boolean isWrittenBy(final Member member) {
        return memberId.isSameMember(member);
    }

    public void updateArticle(final String title, final String content, final String code) {
        this.title = title;
        this.content = content;
        this.code = code;
    }
}
