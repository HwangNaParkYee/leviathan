package com.example.leviathan.service;


import com.example.leviathan.domain.*;
import com.example.leviathan.dto.UpdateCommentDTO;
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
                .orElseThrow(() -> new RuntimeException("게시글 못 찾음"));

        Comment comment = new Comment(article, member, dto.getContent(), LocalDateTime.now(), LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, UpdateCommentDTO dto, HttpServletRequest header) {
        Member member = memberService.tokenToMember(header);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. ID: " + commentId));

        if (!comment.getMemberId().getId().equals(member.getId())) {
            throw new RuntimeException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(dto.getContent());
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
