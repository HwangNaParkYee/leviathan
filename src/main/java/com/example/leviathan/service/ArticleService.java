package com.example.leviathan.service;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.dto.*;
import com.example.leviathan.repository.ArticleRepository;
import com.example.leviathan.repository.CommentRepository;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.repository.ProblemRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;
    private final ProblemRepository problemRepository;
    private final ArticleLikesService articleLikesService;
    private final CommentRepository commentRepository;
    private final MemberService memberService;

    @Transactional
    public ResponseEntity<SaveResDTO> saveArticle(HttpServletRequest header, SaveArticleDTO dto) {
        // 토큰을 이용하여 member 조회
        Member member = memberService.tokenToMember(header);

        Problem problem = problemRepository.findByNumber(dto.getProblemId());

        // 3. Article 엔티티 생성
        Article article = new Article(problem, dto.getTitle(), dto.getContent(), dto.getCode(), member);

        // 4. 저장
        articleRepository.save(article);

        SaveResDTO resDTO = new SaveResDTO(article.getProblemId().getNumber(), article.getTitle(), article.getContent());

        // 5. 성공 응답 반환
        return ResponseEntity.ok(resDTO);
    }



    @Transactional
    public void deleteArticle(HttpServletRequest header, Long articleId) {
        Member member = memberService.tokenToMember(header);

        Article article = articleRepository.findById(articleId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        if(!article.getMemberId().getId().equals(member.getId())) {
            throw new IllegalStateException("본인이 작성한 게시물만 삭제할 수 있습니다.");
        }

        articleRepository.delete(article);
    }



    @Transactional
    public ResponseEntity<String> updateArticle(HttpServletRequest header, ArticleUpdateDTO dto, Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        // 토큰으로 멤버 확인하기
        Member member = memberService.tokenToMember(header);

        if(!article.getMemberId().getId().equals(member.getId())) {
            return ResponseEntity.status(403).body(null);       // 권한이 없을 경우 상태코드 403 반환
        }

        article.updateArticle(dto.getTitle(), dto.getContent(), dto.getCode());
        articleRepository.save(article);

        return ResponseEntity.ok("게시물이 수정되었습니다.");
    }


    public ResponseEntity<FindByArticleDTO> findByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다: " + articleId));

        // DTO 생성자 사용으로 간결하게 처리
        Long likesCount = articleLikesService.countLikes(article);
        FindByArticleDTO dto = new FindByArticleDTO(article, likesCount);

        return ResponseEntity.ok(dto);
    }



    public ResponseEntity<List<FindByAllArticleDTO>> findByAllArticle() {
        List<Article> articles = articleRepository.findAll();

        List<FindByAllArticleDTO> articleDTO = articles.stream()
                .map(article -> {
                    Long likesCount = articleLikesService.countLikes(article);
                    Long commentsCount = commentRepository.countByArticleId(article); // ✅ Article 객체로 변경

                    // Create and return a FindByAllArticleDTO for each article
                    return new FindByAllArticleDTO(
                            article,             // Assuming content or other fields
                            likesCount,                        // The number of likes
                            commentsCount                      // The number of comments
                    );
                })
                .collect(Collectors.toList());


        return ResponseEntity.ok(articleDTO);
    }


    public ResponseEntity<List<FindByProblemDTO>> findByProblemId(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문제를 찾을 수 없습니다."));

        List<Article> articles = articleRepository.findByProblemId(problem);

        List<FindByProblemDTO> articleDTO = articles.stream()
                .map(article -> {
                    Long likesCount = articleLikesService.countLikes(article);
                    Long commentsCount = commentRepository.countByArticleId(article);
                    return new FindByProblemDTO(article, likesCount, commentsCount);
                })
                .collect(Collectors.toList());
        // 나중에 코멘트 개수 로직 추가

        System.out.println(articleDTO + " ----------------------");


        return ResponseEntity.ok(articleDTO);
    }

    //나중에 main 으로 들고 와야 함.

}
