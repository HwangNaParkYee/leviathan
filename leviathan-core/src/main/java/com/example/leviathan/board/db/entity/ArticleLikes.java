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
public class ArticleLikes extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article article;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    private ArticleLikes(final Article article, final Member member) {
        this.article = article;
        this.member = member;
    }

	public static ArticleLikes create(final Article article, final Member member) {
		return new ArticleLikes(article, member);
	}
}
