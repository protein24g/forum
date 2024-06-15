package com.example.forum.boards.freeBoard.image.repository;

import com.example.forum.boards.freeBoard.image.entity.FreeBoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardImageRepository extends JpaRepository<FreeBoardImage, Long> {
    List<FreeBoardImage> findByFreeBoardId(Long boardId);
}
