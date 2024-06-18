package com.example.forum.boards.freeBoard.comment.repository;

import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long> {
    /**
     * 특정 게시글에 대한 댓글 목록을 페이징 한 내용으로 반환
     *
     * @param boardId  게시글 Id
     * @param pageable 페이징
     * @return
     */
    Page<FreeBoardComment> findByFreeBoardId(Long boardId, Pageable pageable);

    /**
     * 특정 유저가 작성한 댓글 목록을 페이징 한 내용으로 반환
     *
     * @param userId   사용자 ID
     * @param pageable 페이징 정보
     * @return
     */
    Page<FreeBoardComment> findByUserId(Long userId, Pageable pageable);

    /**
     * 특정 유저가 댓글 단 글 목록을 페이징 한 내용으로 반환
     *
     * @param userId   사용자 ID
     * @param pageable 페이징 정보
     * @return
     */
    @Query("SELECT DISTINCT b.freeBoard FROM FreeBoardComment b WHERE b.user.id = :userId ORDER BY b.freeBoard.id DESC")
    Page<FreeBoard> getFreeBoardByUserComments(Long userId, Pageable pageable);
}
