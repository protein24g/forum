package com.example.forum.boards.freeBoard.image.repository;


import com.example.forum.boards.freeBoard.image.entity.FreeBoardThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardThumbnailRepository extends JpaRepository<FreeBoardThumbnail, Long> {
    List<FreeBoardThumbnail> findByFreeBoardId(Long boardId);
}
