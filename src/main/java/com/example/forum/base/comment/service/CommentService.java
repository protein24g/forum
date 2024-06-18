package com.example.forum.base.comment.service;

import com.example.forum.base.comment.dto.request.CommentRequest;
import org.springframework.data.domain.Page;

/**
 * 자유 게시판 댓글 인터페이스
 */
public interface CommentService<T> {

    /**
     * 게시글에 댓글 작성
     *
     * @param boardId 게시글 ID
     * @param dto     댓글 요청 DTO
     */
    void createCommentForBoard(Long boardId, CommentRequest dto);

    /**
     * 특정 사용자의 댓글 목록 조회
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @return 댓글 페이지 응답 DTO
     */
    Page<T> getCommentsForUser(Long userId, int page);

    /**
     * 특정 게시글의 댓글 목록 조회
     *
     * @param boardId 게시글 ID
     * @param page    페이지 번호
     * @return 댓글 페이지 응답 DTO
     */
    Page<T> getCommentsForBoard(Long boardId, int page);

    /**
     * 사용자가 댓글 단 게시글 목록을 조회
     *
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @return 게시글 페이지 응답 DTO
     */
    Page<T> getFreeBoardByUserComments(Long userId, int page);

    /**
     * 댓글 수정
     *
     * @param commentId 댓글 ID
     * @param dto       댓글 요청 DTO
     */
    void updateComment(Long commentId, CommentRequest dto);

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 ID
     */
    void deleteComment(Long commentId);
}
