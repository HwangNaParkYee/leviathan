package com.example.leviathan.service;


import com.example.leviathan.domain.*;
import com.example.leviathan.dto.SaveReviewDTO;
import com.example.leviathan.repository.ArticleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

        Article article = articleRepository.findById(saveCommentDTO.getArticleId())
                .orElseThrow(() -> new RuntimeException("문제 못 찾음"));

        Comment comment = new Comment();
        comment.setArticleId(article);
        comment.setMemberId(member);
        comment.setContent(saveCommentDTO.getContent());
        comment.setCreateDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());

        return commentRepository.save(saveComment());
    }
}
