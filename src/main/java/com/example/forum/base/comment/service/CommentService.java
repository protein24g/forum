package com.example.forum.base.comment.service;

import com.example.forum.boards.freeBoard.comment.dto.requests.FreeBoardCommentRequest;
import com.example.forum.user.entity.User;
import org.springframework.data.domain.Page;

// interface : 추상 메서드의 집합
// 클래스가 클래스를 상속 받을 때 : extends
public interface CommentService<T> { // T : 제네릭 타입(어떤 타입이든 허용)
    // C(Create)
    void createCommentForBoard(Long boardId, FreeBoardCommentRequest dto);

    // R(Read)
    Page<T> getCommentsForUser(Long userId, int page);
    Page<T> getCommentsForBoard(Long boardId, int page);
    Page<T> getBoardsByUserComments(User user, int page);
    // U(Update)
    void updateComment(Long commentId, FreeBoardCommentRequest dto);

    // D(Delete)
    void deleteComment(Long commentId);
}
