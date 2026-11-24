package com.example.leviathan.board.facade;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.dto.CommentDTO;
import com.example.leviathan.board.dto.CommentResponseDTO;
import com.example.leviathan.board.dto.FindByArticleIdCommentDTO;
import com.example.leviathan.board.dto.SaveCommentDTO;
import com.example.leviathan.board.dto.UpdateCommentDTO;
import com.example.leviathan.board.service.ArticleService;
import com.example.leviathan.board.service.CommentLikesService;
import com.example.leviathan.board.service.CommentService;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentFacade {

    private final CommentService commentService;
    private final CommentLikesService commentLikesService;
    private final ArticleService articleService;
    private final MemberService memberService;

    //댓글 달기
    @Transactional
    public CommentDTO saveComment(SaveCommentDTO dto, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        Article article = articleService.getById(dto.getArticleId());

        return new CommentDTO(commentService.write(member, article, dto.getContent()));
    }

    //댓글 업데이트
    @Transactional
    public CommentResponseDTO updateComment(Long commentId, UpdateCommentDTO dto, String loginId) {
        Member member = memberService.findByLoginId(loginId);

        return new CommentResponseDTO(commentService.edit(commentId, member, dto.getContent()));
    }

    @Transactional
    public void deleteComment(Long commentId, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        commentService.delete(commentId, member);
    }

    //댓글 찾기
    public List<FindByArticleIdCommentDTO> getCommentsByArticleId(Long articleId) {
        Article article = articleService.getById(articleId);

        return commentService.findByArticle(article).stream()
                .map(comment -> new FindByArticleIdCommentDTO(
                        comment.getId(),
                        comment.getArticleId().getId(),
                        comment.getMemberId().getMemberId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt(),
                        commentLikesService.count(comment)
                ))
                .toList();
    }
}
