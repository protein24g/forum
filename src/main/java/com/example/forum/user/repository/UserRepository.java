package com.example.forum.user.repository;

import com.example.forum.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 회원가입 아이디 중복 체크
     *
     * @param loginId 로그인 ID
     * @return
     */
    boolean existsByLoginId(String loginId);

    /**
     * 회원가입 닉네임 중복 체크
     * 
     * @param nickname 닉네임
     * @return
     */
    boolean existsByNickname(String nickname);

    /**
     * 로그인 아이디로 회원 조회
     *
     * @param loginId 로그인 ID
     * @return
     */
    Optional<User> findByLoginId(String loginId);

    /**
     * 모든 사용자 페이징 결과 조회
     * 
     * @param pageable 페이징
     * @return
     */
    Page<User> findAll(Pageable pageable);

    /**
     * 검색한 키워드가 닉네임에 포함된 유저 목록을 반환
     * 
     * @param keyword  키워드
     * @param pageable 페이징
     * @return
     */
    Page<User> findByNicknameContaining(String keyword, Pageable pageable); // 닉네임 키워드 검색

    /**
     * 검색한 키워드가 로그인 아이디에 포함된 유저 목록을 반환
     *
     * @param keyword  키워드
     * @param pageable 페이징
     * @return
     */
    Page<User> existsByLoginIdContaining(String keyword, Pageable pageable); // 아이디 키워드 검색

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

    /**
     * 사용자 닉네임으로 유저 조회
     * 
     * @param nickname
     * @return
     */
    Optional<User> findByNickname(String nickname);
}
