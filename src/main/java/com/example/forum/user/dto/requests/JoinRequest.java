package com.example.forum.user.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class JoinRequest {
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 8, message = "닉네임은 2자 이상 8자")
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,8}$", message = "한글 초성 및 모음 사용 불가능합니다")
    private String nickname;

    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Size(min = 2, max = 9, message = "아이디는 6자 이상 16자 이하")
    @Pattern(regexp = "^(?=.*[a-z0-9])[a-z0-9]{6,16}$", message = "아이디는 영어 또는 숫자로 구성하세요")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 6, max = 16, message = "비밀번호는 6자 이상 16자 이하")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$])[a-zA-Z0-9!@#$]{6,16}$", message = "영문자, 숫자, 그리고 특수 문자(!@#$)들 중 하나 이상을 포함하여 총 6자 이상, 16자 이하여야 합니다.")
    private String loginPw;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상 입력하세요.")
    private String loginPwCheck;

    private String certificate;

    private String career;

    private LocalDateTime createDate;

    private MultipartFile profile;
}
