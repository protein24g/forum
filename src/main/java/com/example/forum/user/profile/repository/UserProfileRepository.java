package com.example.forum.user.profile.repository;

import com.example.forum.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepository extends JpaRepository<User, Long> {
    /**
     * 특정 사용자의 게시글 개수 조회
     *
     * @param userId 사용자 ID
     * @return
     */
    @Query("SELECT COUNT(b) FROM FreeBoard b WHERE b.user.id = :userId")
    int getUserFreePostCount(Long userId);


    /**
     * 특정 사용자의 댓글 개수 조회
     *
     * @param userId
     * @return
     */
    @Query("SELECT COUNT(c) FROM FreeBoardComment c WHERE c.user.id = :userId")
    int getUserFreeCommentCount(Long userId);
}
