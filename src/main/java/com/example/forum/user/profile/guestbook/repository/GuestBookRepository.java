package com.example.forum.user.profile.guestbook.repository;

import com.example.forum.user.profile.guestbook.entity.GuestBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
    /**
     * 특정 사용자 페이지 방명록 글 목록 페이징 반환
     * @param nickname 특정 사용자 닉네임
     * @param pageable 페이징
     * @return
     */
    @Query("SELECT g FROM GuestBook g WHERE g.targetId = (SELECT u.id FROM User u WHERE u.nickname = :nickname)")
    Page<GuestBook> findByNickname(String nickname, Pageable pageable);

    /**
     * 마이페이지 내가 쓴 방명록 조회
     *
     * @param id       유저 ID
     * @param pageable 페이징 객체
     * @return
     */
    Page<GuestBook> findByUserId(Long id, Pageable pageable);
}
