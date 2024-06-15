package com.example.forum.base.image.repository;

import com.example.forum.base.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByFreeBoardId(Long boardId);
    List<Image> findByQuestionBoardId(Long boardId);
}
