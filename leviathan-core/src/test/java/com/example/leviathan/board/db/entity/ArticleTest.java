package com.example.leviathan.board.db.entity;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    private final Problem problem = new Problem(1L, "title", "question", "hint", 1);
    private final Member writer = memberWithId(1L);
    private final Member other = memberWithId(2L);

    @Test
    @DisplayName("게시글 작성자 확인")
    void article_IsWrittenBy() {
        Article article = new Article(problem, "title", "content", "code", writer);

        assertTrue(article.isWrittenBy(writer));
        assertFalse(article.isWrittenBy(other));
        assertFalse(article.isWrittenBy(null));
    }

    @Test
    @DisplayName("댓글 작성자 확인 및 수정")
    void comment_EditByWriter() {
        Article article = new Article(problem, "title", "content", "code", writer);
        Comment comment = new Comment(article, writer, "content");

        assertTrue(comment.isWrittenBy(writer));
        assertFalse(comment.isWrittenBy(other));
        assertEquals(comment.getCreateDate(), comment.getUpdateDate());

        comment.edit("edited");

        assertEquals("edited", comment.getContent());
        assertFalse(comment.getUpdateDate().isBefore(comment.getCreateDate()));
    }

    private Member memberWithId(Long id) {
        Member member = new Member("user" + id, "encrypted");
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }
}
