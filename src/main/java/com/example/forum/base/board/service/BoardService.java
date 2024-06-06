package com.example.forum.base.board.service;

import com.example.forum.boards.freeBoard.board.dto.requests.FreeBoardRequest;
import com.example.forum.base.board.dto.BoardSearch;
import org.springframework.data.domain.Page;

// interface : 추상 메서드의 집합
// 클래스가 클래스를 상속 받을 때 : extends
public interface BoardService<T> {
    // C(Create)
    T createBoard(FreeBoardRequest dto); // 게시글 생성

    // R(Read)
    Page<T> pageBoards(BoardSearch dto); // 페이지별 게시글 목록 조회

    T getDetail(Long boardNum); // 특정 게시글 상세 조회

    Page<T> getBoardsForUser(Long userId, int page); // 특정 사용자의 게시글 목록 조회

    String getWriter(Long boardNum); // 게시글 작성자 정보 조회

    // U(Update)
    T edit(Long boardNum, FreeBoardRequest dto); // 게시글 수정

    // D(Delete)
}