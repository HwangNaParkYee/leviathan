package com.example.leviathan.service;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.dto.*;
import com.example.leviathan.repository.ArticleRepository;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.repository.ProblemRepository;
import com.example.leviathan.security.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;
    private final ProblemRepository problemRepository;
    private final ArticleLikesService articleLikesService;

    @Transactional
    public ResponseEntity<SaveResDTO> saveArticle(SaveArticleDTO dto, String token) {
        // 토큰을 이용하여 member 조회
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberId(token));

        Problem problem = problemRepository.findByNumber(dto.getNumber());

        // 3. Article 엔티티 생성
        Article article = new Article(problem, dto.getTitle(), dto.getQuestion(), member);

        // 4. 저장
        articleRepository.save(article);

        SaveResDTO resDTO = new SaveResDTO(article.getProblemId().getNumber(), article.getTitle(), article.getContent());

        // 5. 성공 응답 반환
        return ResponseEntity.ok(resDTO);
    }

    @Transactional
    public void deleteArticle(Long articleId, String token) {
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberId(token));

        Article article = articleRepository.findById(articleId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        if(!article.getMemberId().getId().equals(member.getId())) {
            throw new IllegalStateException("본인이 작성한 게시물만 삭제할 수 있습니다.");
        }

        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<FindByArticleDTO> findByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다: " + articleId));

        // DTO 생성자 사용으로 간결하게 처리
        Long likesCount = articleLikesService.countLikes(articleId);
        FindByArticleDTO dto = new FindByArticleDTO(article, likesCount);

        return ResponseEntity.ok(dto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<FindByAllArticleDTO>> findByAllArticle() {
        List<Article> articles = articleRepository.findAll();

        List<FindByAllArticleDTO> articleDTO = articles.stream()
                .map(article -> {
                    Long likesCount = articleLikesService.countLikes(article.getId());
                    return new FindByAllArticleDTO(article, likesCount);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(articleDTO);
    }

    @Transactional
    public ResponseEntity<String> updateArticle(ArticleUpdateDTO dto, String token, Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        // 토큰으로 멤버 확인하기
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberId(token));

        if(!article.getMemberId().getId().equals(member.getId())) {
            return ResponseEntity.status(403).body(null);       // 권한이 없을 경우 상태코드 403 반환
        }

        Long likeCount = articleLikesService.countLikes(article.getId());

        article.updateArticle(dto.getTitle(), dto.getContent());

        FindByArticleDTO dtos = new FindByArticleDTO(article, likeCount);
        return ResponseEntity.ok("게시물이 수정되었습니다.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<FindByProblemDTO>> findByProblemId(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문제를 찾을 수 없습니다."));

        List<Article> articles = articleRepository.findByProblemId(problem);

        List<FindByProblemDTO> articleDTO = articles.stream()
                .map(article -> {
                    Long likesCount = articleLikesService.countLikes(article.getId());
                    return new FindByProblemDTO(article, likesCount);
                })
                .collect(Collectors.toList());
        // 나중에 코멘트 개수 로직 추가

        return ResponseEntity.ok(articleDTO);
    }
}
