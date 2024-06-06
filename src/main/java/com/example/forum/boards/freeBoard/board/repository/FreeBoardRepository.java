package com.example.forum.boards.freeBoard.board.repository;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {
    Page<FreeBoard> findByTitleContaining(String keyword, Pageable pageable);
    Page<FreeBoard> findByContentContaining(String keyword, Pageable pageable);
    Page<FreeBoard> findByUserId(Long userId, Pageable pageable);
}
