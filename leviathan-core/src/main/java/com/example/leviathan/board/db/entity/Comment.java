package com.example.leviathan.board.db.entity;

import com.example.leviathan.member.db.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;

    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentLikes> commentLikesList = new ArrayList<>();

    public Comment(Article article, Member member, String content) {
        this.articleId = article;
        this.memberId = member;
        this.content = content;
        this.createDate = LocalDateTime.now();
        this.updateDate = this.createDate;
    }

    public boolean isWrittenBy(Member member) {
        return memberId.isSameMember(member);
    }

    public void edit(String content) {
        this.content = content;
        this.updateDate = LocalDateTime.now();
    }
}
