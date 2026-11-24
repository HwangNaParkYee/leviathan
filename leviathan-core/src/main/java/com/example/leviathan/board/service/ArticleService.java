package com.example.leviathan.board.service;

import com.example.leviathan.board.exception.BoardErrorCode;
import com.example.leviathan.board.exception.BoardException;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.db.repository.ArticleRepository;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article getById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.ARTICLE_NOT_FOUND));
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findByProblem(Problem problem) {
        return articleRepository.findByProblemId(problem);
    }

    @Transactional
    public Article write(Member member, Problem problem, String title, String content, String code) {
        return articleRepository.save(new Article(problem, title, content, code, member));
    }

    @Transactional
    public Article update(Long articleId, Member member, String title, String content, String code) {
        Article article = getById(articleId);

        if (!article.isWrittenBy(member)) {
            throw new BoardException(BoardErrorCode.ARTICLE_ACCESS_DENIED);
        }

        article.updateArticle(title, content, code);
        return articleRepository.save(article);
    }

    // 작성자 본인만 삭제 가능
    @Transactional
    public void delete(Long articleId, Member member) {
        Article article = getById(articleId);

        if (!article.isWrittenBy(member)) {
            throw new BoardException(BoardErrorCode.ARTICLE_ACCESS_DENIED);
        }

        articleRepository.delete(article);
    }
}
