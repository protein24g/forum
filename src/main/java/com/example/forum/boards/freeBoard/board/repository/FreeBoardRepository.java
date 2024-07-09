package com.example.forum.boards.freeBoard.board.repository;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 자유 게시판 인터페이스
 */
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {
    /**
     * 검색한 키워드가 제목에 포함된 게시글 목록을 반환
     * 
     * @param keyword  검색 키워드
     * @param pageable 페이징 정보
     * @return
     */
    Page<FreeBoard> findByTitleContaining(String keyword, Pageable pageable);

    /**
     * 검색한 키워드가 내용에 포함된 게시글 목록을 반환
     * 
     * @param keyword
     * @param pageable
     * @return
     */
    Page<FreeBoard> findByContentContaining(String keyword, Pageable pageable);

    /**
     * 특정 사용자의 게시글 목록 조회
     * 
     * @param userId
     * @param pageable
     * @return
     */
    Page<FreeBoard> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT fb FROM FreeBoard fb LEFT JOIN fb.freeBoardComments fbc GROUP BY fb ORDER BY COUNT(fbc) DESC")
    Page<FreeBoard> findAllOrderByCommentCountDesc(Pageable pageable);
}
