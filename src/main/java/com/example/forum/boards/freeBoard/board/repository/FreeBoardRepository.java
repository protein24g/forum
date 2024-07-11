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
     * 모든 게시글 목록을 반환
     *
     * @param pageable
     * @return
     */
    Page<FreeBoard> findAll(Pageable pageable);

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

    /**
     * 게시판 댓글순 정렬된 리스트 반환
     * 
     * @param pageable
     * @return
     */
    @Query("SELECT fb FROM FreeBoard fb LEFT JOIN FreeBoardComment fbc on fb.id = fbc.freeBoard.id GROUP BY fb.id ORDER BY COUNT(fbc) DESC, fb.id DESC")
    Page<FreeBoard> findAllOrderByCommentCountDesc(Pageable pageable);

    /**
     * 게시판 제목 키워드 검색 후 댓글순 정렬된 리스트 반환
     *
     * @param keyword 검색 키워드
     * @param pageable
     * @return
     */
    @Query("SELECT fb FROM FreeBoard fb LEFT JOIN FreeBoardComment fbc on fb.id = fbc.freeBoard.id WHERE fb.title LIKE %:keyword% GROUP BY fb.id ORDER BY COUNT(fbc) DESC, fb.id DESC")
    Page<FreeBoard> findAllByTitleOrderByCommentCountDesc(String keyword, Pageable pageable);

    /**
     * 게시판 내용 키워드 검색 후 댓글순 정렬된 리스트 반환
     *
     * @param keyword 검색 키워드
     * @param pageable
     * @return
     */
    @Query("SELECT fb FROM FreeBoard fb LEFT JOIN FreeBoardComment fbc on fb.id = fbc.freeBoard.id WHERE fb.content LIKE %:keyword% GROUP BY fb.id ORDER BY COUNT(fbc) DESC, fb.id DESC")
    Page<FreeBoard> findAllByContentOrderByCommentCountDesc(String keyword, Pageable pageable);

    /**
     * 게시판 좋아요순 정렬된 리스트 반환
     *
     * @param pageable
     * @return
     */
    @Query("SELECT fb FROM FreeBoard fb LEFT JOIN UserLike ul on fb.id = ul.freeBoard.id GROUP BY fb.id ORDER BY COUNT(ul) DESC, fb.id DESC")
    Page<FreeBoard> findAllOrderByLikeCountDesc(Pageable pageable);

    /**
     * 게시판 제목 키워드 검색 후 좋아요순 정렬된 리스트 반환
     *
     * @param keyword 검색 키워드
     * @param pageable
     * @return
     */
    @Query("SELECT fb FROM FreeBoard fb LEFT JOIN UserLike ul on fb.id = ul.freeBoard.id WHERE fb.title LIKE %:keyword% GROUP BY fb.id ORDER BY COUNT(ul) DESC, fb.id DESC")
    Page<FreeBoard> findAllByTitleOrderByLikeCountDesc(String keyword, Pageable pageable);

    /**
     * 게시판 내용 키워드 검색 후 좋아요순 정렬된 리스트 반환
     *
     * @param keyword 검색 키워드
     * @param pageable
     * @return
     */
    @Query("SELECT fb FROM FreeBoard fb LEFT JOIN UserLike ul on fb.id = ul.freeBoard.id WHERE fb.content LIKE %:keyword% GROUP BY fb.id ORDER BY COUNT(ul) DESC, fb.id DESC")
    Page<FreeBoard> findAllByContentOrderByLikeCountDesc(String keyword, Pageable pageable);
}
