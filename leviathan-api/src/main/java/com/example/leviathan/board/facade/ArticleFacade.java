package com.example.leviathan.board.facade;


import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.controller.request.UpdateArticleRequest;
import com.example.leviathan.board.facade.response.FindByAllArticleResponse;
import com.example.leviathan.board.facade.response.FindByArticleResponse;
import com.example.leviathan.board.facade.response.FindByProblemResponse;
import com.example.leviathan.board.controller.request.SaveArticleRequest;
import com.example.leviathan.board.facade.response.SaveArticleResponse;
import com.example.leviathan.board.service.ArticleLikesService;
import com.example.leviathan.board.service.ArticleService;
import com.example.leviathan.board.service.CommentService;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleFacade {

    private final ArticleService articleService;
    private final ArticleLikesService articleLikesService;
    private final CommentService commentService;
    private final ProblemService problemService;
    private final MemberService memberService;

    @Transactional
    public SaveArticleResponse saveArticle(String loginId, SaveArticleRequest dto) {
        Member member = memberService.findByLoginId(loginId);

        Problem problem = problemService.findByNumber(dto.problemId()).orElse(null);

        Article article = articleService.write(member, problem, dto.title(), dto.content(), dto.code());

        return SaveArticleResponse.from(article);
    }

    @Transactional
    public void deleteArticle(String loginId, Long articleId) {
        Member member = memberService.findByLoginId(loginId);
        articleService.delete(articleId, member);
    }

    @Transactional
    public void updateArticle(String loginId, UpdateArticleRequest dto, Long articleId) {
        Member member = memberService.findByLoginId(loginId);

        articleService.update(articleId, member, dto.title(), dto.content(), dto.code());
    }

    public FindByArticleResponse findByArticle(Long articleId) {
        Article article = articleService.getById(articleId);
        return FindByArticleResponse.of(article, articleLikesService.count(article));
    }

    public List<FindByAllArticleResponse> findByAllArticle() {
		return articleService.findAll().stream()
				.map(article -> FindByAllArticleResponse.of(
						article,
						articleLikesService.count(article),
						commentService.countByArticle(article)
				))
				.toList();
    }

    public List<FindByProblemResponse> findByProblemId(Long problemId) {
        Problem problem = problemService.getById(problemId);

        return articleService.findByProblem(problem).stream()
                .map(article -> FindByProblemResponse.of(
                        article,
                        articleLikesService.count(article),
                        commentService.countByArticle(article)
                ))
                .toList();
    }
}
