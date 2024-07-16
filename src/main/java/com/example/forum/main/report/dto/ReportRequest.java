package com.example.forum.main.report.dto;

import com.example.forum.main.report.entity.Report;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportRequest {
    private Long postId; // 신고 대상 게시글의 ID
    private Long commentId; // 신고 대상 댓글의 ID
    private Long guestBookId; // 신고 대상 방명록의 ID
    private Report.ReasonType reason; // 신고 유형
    private String notes; // 신고 내용
}
