package com.example.leviathan.service;


import com.example.leviathan.domain.*;
import com.example.leviathan.dto.*;
import com.example.leviathan.repository.ArticleRepository;
import com.example.leviathan.repository.CommentLikesRepository;
import com.example.leviathan.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberService memberService;
    private final CommentLikesRepository commentLikesRepository;


    //댓글 달기
    @Transactional
    public CommentDTO saveComment(SaveCommentDTO dto, HttpServletRequest header) {
        Member member = memberService.tokenToMember(header);
        Article article = articleRepository.findById(dto.getArticleId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다: ID=" + dto.getArticleId()));

        Comment comment = new Comment(article, member, dto.getContent(), LocalDateTime.now(), LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        return new CommentDTO(savedComment);
    }

    //댓글 업데이트
    @Transactional
    public CommentResponseDTO updateComment(Long commentId, UpdateCommentDTO dto, HttpServletRequest header) {
        Member member = memberService.tokenToMember(header);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. ID: " + commentId));

        if (!comment.getMemberId().getId().equals(member.getId())) {
            throw new RuntimeException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(dto.getContent());
        comment.setUpdateDate(LocalDateTime.now());

        return new CommentResponseDTO(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long commentId, HttpServletRequest header) {
        Member member = memberService.tokenToMember(header); // JWT에서 현재 로그인한 사용자 정보 가져오기

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. ID: " + commentId));

        if (!comment.getMemberId().getId().equals(member.getId())) {
            throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment); // 댓글 삭제
    }

    // 게시물에 대한 댓글 개수 반환
    @Transactional
    public Long getCommentCount(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        return commentRepository.countByArticleId(article);
    }

    //댓글 찾기
    public List<FindByArticleIdCommentDTO> getCommentsByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + articleId));

        List<Comment> comments = commentRepository.findByArticleId(article);

        return comments.stream()
                .map(comment -> new FindByArticleIdCommentDTO(
                        comment.getId(),
                        comment.getArticleId().getId(),
                        comment.getMemberId().getMemberId(),
                        comment.getContent(),
                        comment.getCreateDate(),
                        comment.getUpdateDate(),
                        commentLikesRepository.countByComment(comment)
                ))
                .toList();
    }
}
