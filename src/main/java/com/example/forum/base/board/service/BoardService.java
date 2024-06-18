package com.example.forum.base.board.service;

import org.springframework.data.domain.Page;

/**
 * 자유 게시판 서비스 인터페이스
 */
public interface BoardService<T, Req, Res, Search> {
    /**
     * 게시글 생성
     *
     * @param dto 게시글 요청 DTO
     * @return 생성된 게시글 응답 DTO
     */
    Res create(Req dto);

    /**
     * 게시글 목록 조회
     *
     * @param dto 검색 조건 DTO
     * @return 게시글 페이지 응답 DTO
     */
    Page<Res> boardPage(Search dto);

    /**
     * 게시글 상세 조회
     *
     * @param boardId 게시글 ID
     * @return 게시글 응답 DTO
     */
    Res getDetail(Long boardId);

    /**
     * 특정 사용자의 게시글 목록 조회
     *
     * @param userId 사용자 ID
     * @param page   페이지 번호
     * @return 게시글 페이지 응답 DTO
     */
    Page<Res> getBoardsForUser(Long userId, int page);

    /**
     * 게시글 작성자 닉네임 조회
     *
     * @param boardId 게시글 ID
     * @return 작성자 닉네임
     */
    String getWriter(Long boardId);

    /**
     * 내가 쓴 글인지 체크
     *
     * @param boardId 게시글 ID
     */
    void writerCheck(Long boardId);

    /**
     * 게시글 수정 데이터 가져오기
     *
     * @param boardId 게시글 ID
     * @return 수정할 게시글 응답 DTO
     */
    Res getBoardUpdateData(Long boardId);

    /**
     * 게시글 수정
     *
     * @param boardId 게시글 ID
     * @param dto     게시글 요청 DTO
     * @return 수정된 게시글 응답 DTO
     */
    Res update(Long boardId, Req dto);

    /**
     * 게시글 삭제
     *
     * @param boardId 게시글 ID
     */
    void delete(Long boardId);
}
