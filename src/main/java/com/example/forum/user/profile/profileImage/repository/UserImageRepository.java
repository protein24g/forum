package com.example.forum.user.profile.profileImage.repository;

import com.example.forum.user.profile.profileImage.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
}
