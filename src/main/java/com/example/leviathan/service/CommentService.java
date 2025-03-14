package com.example.leviathan.service;


import com.example.leviathan.domain.*;
import com.example.leviathan.dto.SaveReviewDTO;
import com.example.leviathan.dto.saveCommentDTO;
import com.example.leviathan.repository.ArticleRepository;
import com.example.leviathan.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;
    private ArticleRepository articleRepository;
    private MemberService memberService;

    @Transactional
    public Comment saveComment(saveCommentDTO dto, HttpServletRequest header) {

        Member member = memberService.tokenToMember(header);

        Article article = articleRepository.findById(dto.getArticleId())
                .orElseThrow(() -> new RuntimeException("문제 못 찾음"));

        Comment comment = new Comment();
        comment.setArticleId(article);
        comment.setMemberId(member);
        comment.setContent(dto.getContent());
        comment.setCreateDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    // 게시물에 대한 댓글 개수 반환
    @Transactional
    public Long getCommentCount(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        Long commentCount = commentRepository.countByArticleId(articleId);

        return commentCount;
    }
}
