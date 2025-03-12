package com.example.leviathan.service;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.dto.ArticleUpdateDTO;
import com.example.leviathan.dto.FindByAllArticleDTO;
import com.example.leviathan.dto.FindByArticleDTO;
import com.example.leviathan.dto.SaveArticleDTO;
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

    @Transactional
    public ResponseEntity<String> saveArticle(SaveArticleDTO dto, String token) {
        // 토큰을 이용하여 member 조회
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberId(token));

        Problem problem = problemRepository.findByNumber(dto.getNumber());

        // 3. Article 엔티티 생성
        Article article = new Article(problem, dto.getTitle(), dto.getQuestion(), member);

        // 4. 저장
        articleRepository.save(article);

        // 5. 성공 응답 반환
        return ResponseEntity.ok("게시글이 성공적으로 저장되었습니다.");
    }

    @Transactional
    public void deleteArticle(String token, Long articleId) {
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberId(token));

        Article article = articleRepository.findById(articleId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        if(!article.getMemberId().getId().equals(member.getId())) {
            throw new IllegalStateException("본인이 작성한 게시물만 삭제할 수 있습니다.");
        }

        articleRepository.delete(article);
    }

    @Transactional
    public ResponseEntity<FindByArticleDTO> findByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다: " + articleId));

        // DTO 생성자 사용으로 간결하게 처리
        FindByArticleDTO dto = new FindByArticleDTO(article);

        return ResponseEntity.ok(dto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<FindByAllArticleDTO>> findByAllArticle() {
        List<Article> articles = articleRepository.findAll();

        List<FindByAllArticleDTO> articleDTOS = articles.stream()
                .map(FindByAllArticleDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(articleDTOS);
    }

    @Transactional
    public ResponseEntity<FindByArticleDTO> updateArticle(ArticleUpdateDTO dto, String token) {
        Article article = articleRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다." + dto.getId()));

        // 토큰으로 멤버 확인하기
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberId(token));

        if(!article.getMemberId().getId().equals(member.getId())) {
            return ResponseEntity.status(403).body(null);       // 권한이 없을 경우 상태코드 403 반환
        }

        article.updateArticle(dto.getTitle(), dto.getContent());

        FindByArticleDTO dtos = new FindByArticleDTO(article);
        return ResponseEntity.ok(dtos);
    }

//    @Transactional(readOnly = true)
//    public ResponseEntity<List<FindByProblemDTO>> findByProblemId(Long problemId) {

//    }
}
