package com.example.forum.base.board.service;

import com.example.forum.base.board.dto.BoardRequest;
import com.example.forum.base.board.dto.BoardSearch;
import org.springframework.data.domain.Page;

// interface : 추상 메소드의 집합
// 클래스가 클래스를 상속 받을 때 : extends
/**
 * 게시판 서비스를 정의하는 인터페이스
 *
 * @param <T> 게시글 엔터티의 유형을 지정하는 제네릭 타입
 */
public interface BoardService<T> {

    /**
     * 새로운 게시글을 생성
     *
     * @param dto 새로운 게시글의 정보가 담긴 DTO
     * @return 생성된 게시글 엔터티
     */
    T create(BoardRequest dto);

    /**
     * 게시글 목록을 페이지 단위로 반환
     *
     * @param dto 게시글 검색을 위한 검색 조건이 담긴 DTO
     * @return 페이지별 게시글 목록
     */
    Page<T> boardPage(BoardSearch dto);

    /**
     * 특정 게시글의 상세 정보를 조회
     *
     * @param boardNum 조회할 게시글의 고유 번호
     * @return 조회된 게시글의 상세 정보
     */
    T getDetail(Long boardNum);

    /**
     * 특정 사용자가 작성한 게시글 목록을 페이지 단위로 반환
     *
     * @param userId 사용자의 고유 식별자
     * @param page   페이지 번호
     * @return 특정 사용자가 작성한 게시글 목록
     */
    Page<T> getBoardsForUser(Long userId, int page);

    /**
     * 특정 게시글의 작성자를 반환
     *
     * @param boardNum 게시글의 고유 번호
     * @return 게시글의 작성자
     */
    String getWriter(Long boardNum);

    /**
     * 게시글을 업데이트
     *
     * @param boardNum 게시글의 고유 번호
     * @param dto      업데이트할 게시글의 정보가 담긴 DTO
     * @return 업데이트된 게시글 엔터티
     */
    T update(Long boardNum, BoardRequest dto);
}
