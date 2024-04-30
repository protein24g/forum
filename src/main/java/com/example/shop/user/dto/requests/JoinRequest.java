package com.example.shop.user.dto.requests;

import com.example.shop.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class JoinRequest {
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 12, message = "닉네임은 2자 이상 12자 이하이어야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 한글, 영문, 숫자만 사용 가능합니다.")
    private String nickname;

    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Pattern(regexp = "^[a-z]+[a-z0-9]{5,15}$", message = "아이디는 소문자로 시작하고 소문자와 숫자를 포함해 6자 이상 15자 이하이어야 합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[!@#$%^&*]).{8,}$", message = "비밀번호는 특수문자를 포함해 8자 이상이어야 합니다.")
    private String loginPw;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상 입력하세요.")
    private String loginPwCheck;

    private LocalDateTime createDate;

    private int age;

    private User.Gender gender;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    private User.Role role;
}
