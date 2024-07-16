package com.example.forum.main.report.entity;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
import com.example.forum.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "free_Board_id")
    private FreeBoard freeBoard;

    @ManyToOne
    @JoinColumn(name = "free_Board_Comment_id")
    private FreeBoardComment freeBoardComment;

    @Enumerated(EnumType.STRING)
    private ReasonType reason;

    private String notes; // 부가 설명

    private LocalDateTime reportDate;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    private String response;

    public enum ReasonType {
        SPAM_AD("스팸 / 광고 / 도배"),
        INSULT("욕설 / 모욕"),
        ILLEGAL("불법 활동"),
        HATE("혐오 발언"),
        PORN("음란물"),
        PRIVACY("개인정보 노출"),
        OTHER("기타");

        private final String reasonName;

        ReasonType(String reasonName) {
            this.reasonName = reasonName;
        }

        public String getReasonName() {
            return reasonName;
        }
    }

    public enum StatusType {
        PENDING("접수 중"),
        WORKING("처리 중"),
        COMPLETED("해결 완료"),
        CANCELLED("취소");

        private final String description;

        StatusType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Builder
    public Report(ReasonType reason, String notes, LocalDateTime reportDate, StatusType status, String response) {
        this.reason = reason;
        this.notes = notes;
        this.reportDate = reportDate;
        this.status = status;
        this.response = response;
    }

    public void setUserReport(User user) {
        this.user = user;
    }

    public void setFreeBoardReport(FreeBoard freeBoard) {
        this.freeBoard = freeBoard;
    }

    public void setFreeBoardCommentReport(FreeBoardComment freeBoardComment) {
        this.freeBoardComment = freeBoardComment;
    }
}
