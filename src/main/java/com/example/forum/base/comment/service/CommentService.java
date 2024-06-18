package com.example.forum.base.comment.service;

import com.example.forum.base.comment.dto.request.CommentRequest;
import com.example.forum.user.entity.User;
import org.springframework.data.domain.Page;

// interface : 추상 메소드의 집합
// 클래스가 클래스를 상속 받을 때 : extends
/**
 * 댓글 서비스를 정의하는 인터페이스
 *
 * @param <T> 댓글 엔터티의 유형을 지정하는 제네릭 타입
 */
public interface CommentService<T> {

    /**
     * 게시글에 댓글을 생성
     *
     * @param boardId 게시글의 고유 번호
     * @param dto     생성할 댓글의 정보가 담긴 DTO
     */
    void createCommentForBoard(Long boardId, CommentRequest dto);

    /**
     * 특정 사용자가 작성한 댓글 목록을 페이지 단위로 반환
     *
     * @param userId 사용자의 고유 식별자
     * @param page   페이지 번호
     * @return 특정 사용자가 작성한 댓글 목록
     */
    Page<T> getCommentsForUser(Long userId, int page);

    /**
     * 특정 게시글에 작성된 댓글 목록을 페이지 단위로 반환
     *
     * @param boardId 게시글의 고유 번호
     * @param page    페이지 번호
     * @return 특정 게시글에 작성된 댓글 목록
     */
    Page<T> getCommentsForBoard(Long boardId, int page);

    /**
     * 특정 사용자가 작성한 댓글을 포함하는 게시글 목록을 페이지 단위로 반환
     *
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @return 특정 사용자가 작성한 댓글을 포함하는 게시글 목록
     */
    Page<T> getFreeBoardByUserComments(Long userId, int page);

    /**
     * 댓글을 업데이트
     *
     * @param commentId 댓글의 고유 번호
     * @param dto       업데이트할 댓글의 정보가 담긴 DTO
     */
    void updateComment(Long commentId, CommentRequest dto);

    /**
     * 댓글을 삭제
     *
     * @param commentId 삭제할 댓글의 고유 번호
     */
    void deleteComment(Long commentId);
}
