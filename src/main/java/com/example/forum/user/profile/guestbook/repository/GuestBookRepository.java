package com.example.forum.user.profile.guestbook.repository;

import com.example.forum.user.profile.guestbook.entity.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

}
