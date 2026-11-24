package com.example.leviathan.board.facade;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.facade.response.SaveCommentResponse;
import com.example.leviathan.board.facade.response.UpdateCommentResponse;
import com.example.leviathan.board.facade.response.FindByArticleIdCommentResponse;
import com.example.leviathan.board.controller.request.SaveCommentRequest;
import com.example.leviathan.board.controller.request.UpdateCommentRequest;
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
    public SaveCommentResponse saveComment(SaveCommentRequest dto, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        Article article = articleService.getById(dto.articleId());

        return SaveCommentResponse.from(commentService.write(member, article, dto.content()));
    }

    //댓글 업데이트
    @Transactional
    public UpdateCommentResponse updateComment(Long commentId, UpdateCommentRequest dto, String loginId) {
        Member member = memberService.findByLoginId(loginId);

        return UpdateCommentResponse.from(commentService.edit(commentId, member, dto.content()));
    }

    @Transactional
    public void deleteComment(Long commentId, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        commentService.delete(commentId, member);
    }

    //댓글 찾기
    public List<FindByArticleIdCommentResponse> getCommentsByArticleId(Long articleId) {
        Article article = articleService.getById(articleId);

        return commentService.findByArticle(article).stream()
                .map(comment -> new FindByArticleIdCommentResponse(
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
