package com.example.leviathan.member.db.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberPk")
    private Member member;

    private String refreshToken;

    private LocalDateTime expiryDate;

    public static RefreshToken issue(Member member, String refreshToken, LocalDateTime expiryDate) {
        RefreshToken token = new RefreshToken();
        token.member = member;
        token.refreshToken = refreshToken;
        token.expiryDate = expiryDate;
        return token;
    }

    public boolean isExpired(LocalDateTime now) {
        return expiryDate == null || !expiryDate.isAfter(now);
    }

    public void rotate(String newRefreshToken, LocalDateTime newExpiryDate) {
        this.refreshToken = newRefreshToken;
        this.expiryDate = newExpiryDate;
    }
}
