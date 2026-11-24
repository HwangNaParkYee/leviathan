package com.example.leviathan.board.service;

import com.example.leviathan.board.exception.BoardErrorCode;
import com.example.leviathan.board.exception.BoardException;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.db.entity.Comment;
import com.example.leviathan.board.db.repository.CommentRepository;
import com.example.leviathan.member.db.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment getById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.COMMENT_NOT_FOUND));
    }

    public List<Comment> findByArticle(Article article) {
        return commentRepository.findByArticleId(article);
    }

    // 게시물에 대한 댓글 개수 반환
    public Long countByArticle(Article article) {
        return commentRepository.countByArticleId(article);
    }

    //댓글 달기
    @Transactional
    public Comment write(Member member, Article article, String content) {
        return commentRepository.save(Comment.create(article, member, content));
    }

    //댓글 업데이트
    @Transactional
    public Comment edit(Long commentId, Member member, String content) {
        Comment comment = getById(commentId);

        if (!comment.isWrittenBy(member)) {
            throw new BoardException(BoardErrorCode.COMMENT_ACCESS_DENIED);
        }

        comment.edit(content);
        return commentRepository.save(comment);
    }

    //댓글 삭제
    @Transactional
    public void delete(Long commentId, Member member) {
        Comment comment = getById(commentId);

        if (!comment.isWrittenBy(member)) {
            throw new BoardException(BoardErrorCode.COMMENT_ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }
}
