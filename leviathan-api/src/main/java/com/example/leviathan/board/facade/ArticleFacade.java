package com.example.leviathan.board.facade;

import com.example.leviathan.board.exception.BoardErrorCode;
import com.example.leviathan.board.exception.BoardException;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.dto.ArticleUpdateDTO;
import com.example.leviathan.board.dto.FindByAllArticleDTO;
import com.example.leviathan.board.dto.FindByArticleDTO;
import com.example.leviathan.board.dto.FindByProblemDTO;
import com.example.leviathan.board.dto.SaveArticleDTO;
import com.example.leviathan.board.dto.SaveResDTO;
import com.example.leviathan.board.service.ArticleLikesService;
import com.example.leviathan.board.service.ArticleService;
import com.example.leviathan.board.service.CommentService;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
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
    public ResponseEntity<SaveResDTO> saveArticle(String loginId, SaveArticleDTO dto) {
        Member member = memberService.findByLoginId(loginId);

        // 요청의 problemId는 문제 번호(number)다
        Problem problem = problemService.findByNumber(dto.getProblemId()).orElse(null);

        Article article = articleService.write(member, problem, dto.getTitle(), dto.getContent(), dto.getCode());

        return ResponseEntity.ok(new SaveResDTO(article.getProblemId().getNumber(), article.getTitle(), article.getContent()));
    }

    @Transactional
    public void deleteArticle(String loginId, Long articleId) {
        Member member = memberService.findByLoginId(loginId);
        articleService.delete(articleId, member);
    }

    // 서비스가 던진 예외를 여기서 응답으로 바꾸므로 Facade에서는 트랜잭션을 열지 않는다
    // (트랜잭션을 열면 rollback-only로 표시되어 응답 대신 500이 나간다)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResponseEntity<String> updateArticle(String loginId, ArticleUpdateDTO dto, Long articleId) {
        Member member = memberService.findByLoginId(loginId);

        try {
            articleService.update(articleId, member, dto.getTitle(), dto.getContent(), dto.getCode());
        } catch (BoardException e) {
            if (e.getErrorCode() != BoardErrorCode.ARTICLE_ACCESS_DENIED) {
                throw e;
            }
            return ResponseEntity.status(403).body(null);       // 권한이 없을 경우 상태코드 403 반환
        }

        return ResponseEntity.ok("게시물이 수정되었습니다.");
    }

    public ResponseEntity<FindByArticleDTO> findByArticle(Long articleId) {
        Article article = articleService.getById(articleId);
        return ResponseEntity.ok(new FindByArticleDTO(article, articleLikesService.count(article)));
    }

    public ResponseEntity<List<FindByAllArticleDTO>> findByAllArticle() {
        List<FindByAllArticleDTO> articleDTO = articleService.findAll().stream()
                .map(article -> new FindByAllArticleDTO(
                        article,
                        articleLikesService.count(article),     // 좋아요 수
                        commentService.countByArticle(article)  // 댓글 수
                ))
                .toList();

        return ResponseEntity.ok(articleDTO);
    }

    public ResponseEntity<List<FindByProblemDTO>> findByProblemId(Long problemId) {
        Problem problem = problemService.getById(problemId);

        List<FindByProblemDTO> articleDTO = articleService.findByProblem(problem).stream()
                .map(article -> new FindByProblemDTO(
                        article,
                        articleLikesService.count(article),
                        commentService.countByArticle(article)
                ))
                .toList();

        return ResponseEntity.ok(articleDTO);
    }
}
