package com.example.forum.user.profile.repository;

import com.example.forum.user.entity.User;
import com.example.forum.user.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
    @Query("SELECT u FROM UserLike u WHERE u.user = :user and u.freeBoard.id = :boardId")
    Optional<UserLike> findPostsILiked(User user, Long boardId);

    @Query("SELECT u FROM UserLike u WHERE u.user = :user and u.freeBoardComment.id = :commentId")
    Optional<UserLike> findCommentsILiked(User user, Long commentId);
}
