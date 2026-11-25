package com.example.leviathan.board.service;

import com.example.leviathan.board.exception.BoardErrorCode;
import com.example.leviathan.board.exception.BoardException;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.db.repository.ArticleRepository;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    private final Problem problem = new Problem(1L, "title", "question", "hint", 1);
    private final Member writer = memberWithId(1L);
    private final Member other = memberWithId(2L);

    @Test
    @DisplayName("작성자는 게시글을 수정할 수 있다")
    void update_ByWriter() {
        Article article = new Article(problem, "title", "content", "code", writer);
        when(articleRepository.findById(10L)).thenReturn(Optional.of(article));
        when(articleRepository.save(article)).thenReturn(article);

        Article updated = articleService.update(10L, writer, "newTitle", "newContent", "newCode");

        assertEquals("newTitle", updated.getTitle());
        assertEquals("newContent", updated.getContent());
    }

    @Test
    @DisplayName("작성자가 아니면 게시글을 수정/삭제할 수 없다")
    void updateAndDelete_ByOther() {
        Article article = new Article(problem, "title", "content", "code", writer);
        when(articleRepository.findById(10L)).thenReturn(Optional.of(article));

        BoardException onUpdate = assertThrows(BoardException.class, () -> articleService.update(10L, other, "x", "x", "x"));
        BoardException onDelete = assertThrows(BoardException.class, () -> articleService.delete(10L, other));

        assertEquals(BoardErrorCode.ARTICLE_ACCESS_DENIED, onUpdate.getErrorCode());
        assertEquals(BoardErrorCode.ARTICLE_ACCESS_DENIED, onDelete.getErrorCode());

        assertEquals("title", article.getTitle());
        verify(articleRepository, never()).delete(any());
    }

    private Member memberWithId(Long id) {
        Member member = new Member("user" + id, "encrypted");
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }
}
