package com.example.forum.admin.service;

import com.example.forum.admin.dto.response.AdminResponse;
import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.user.entity.User;
import com.example.forum.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    /**
     * 관리자용으로 모든 사용자 목록 조회
     *
     * @param keyword 검색 키워드
     * @param page    페이지 번호
     * @param option  검색 옵션
     * @return 사용자 목록 응답 페이지 DTO
     */
    public Page<AdminResponse> getAllUsersForAdmin(String keyword, int page, String option){
        if(authenticationService.isAdmin()){
            Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
            Page<User> users = null;
            if(keyword.length() != 0){ // 키워드가 있으면
                switch (option){
                    case "1": // 1 닉네임
                        users = userRepository.findByNicknameContaining(keyword, pageable);
                        break;
                    case "2": // 2 아이디
                        users = userRepository.existsByLoginIdContaining(keyword, pageable);
                        break;
                }
            }else{
                users = userRepository.findAll(pageable);
            }
            return users.map(user -> AdminResponse.builder()
                    .id(user.getId())
                    .role(String.valueOf(user.getRole()))
                    .nickname(user.getNickname())
                    .userId(user.getLoginId())
                    .createDate(user.getCreateDate())
                    .isActive(user.getActive())
                    .build());
        } else {
            throw new IllegalArgumentException("관리자 권한이 필요합니다");
        }
    }
}
