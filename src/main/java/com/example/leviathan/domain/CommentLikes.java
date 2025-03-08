package com.example.leviathan.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CommentLikes {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentId")
    private Comment commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;
}
