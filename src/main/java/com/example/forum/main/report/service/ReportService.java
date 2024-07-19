package com.example.forum.main.report.service;

import com.example.forum.base.auth.service.AuthenticationService;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.board.repository.FreeBoardRepository;
import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
import com.example.forum.boards.freeBoard.comment.repository.FreeBoardCommentRepository;
import com.example.forum.main.report.dto.ReportRequest;
import com.example.forum.main.report.entity.Report;
import com.example.forum.main.report.repository.ReportRepository;
import com.example.forum.user.auth.dto.requests.CustomUserDetails;
import com.example.forum.user.auth.repository.UserAuthRepository;
import com.example.forum.user.entity.User;
import com.example.forum.user.profile.guestbook.entity.GuestBook;
import com.example.forum.user.profile.guestbook.repository.GuestBookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final AuthenticationService authenticationService;
    private final UserAuthRepository userAuthRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final GuestBookRepository guestBookRepository;
    private final ReportRepository reportRepository;

    public void reportPosts(ReportRequest dto){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if (customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            FreeBoard freeBoard = freeBoardRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

            // 게시글 신고 여부 확인
            Long boardId = dto.getPostId();
            boolean alreadyReported = user.getReports().stream()
                    .anyMatch(report -> report.getFreeBoard() != null && report.getFreeBoard().getId().equals(boardId));

            // 내가 쓴 글 여부 확인
            if (freeBoard.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("내가 쓴 게시글은 신고할 수 없습니다");
            }

            if (alreadyReported){
                throw new IllegalArgumentException("이미 신고한 게시글입니다");
            } else {
                Report report = Report.builder()
                        .reason(dto.getReason())
                        .notes(dto.getNotes())
                        .reportDate(LocalDateTime.now())
                        .status(Report.StatusType.PENDING)
                        .build();
                reportRepository.save(report);
                user.addReports(report);
                freeBoard.addReports(report);
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    public void reportComments(ReportRequest dto){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(dto.getCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다"));

            // 댓글 신고 여부 확인
            Long commentId = dto.getCommentId();
            boolean alreadyReported = user.getReports().stream()
                    .anyMatch(report -> report.getFreeBoardComment() != null && report.getFreeBoardComment().getId().equals(commentId));

            if(alreadyReported){
                throw new IllegalArgumentException("이미 신고한 댓글입니다");
            } else {
                Report report = Report.builder()
                        .reason(dto.getReason())
                        .notes(dto.getNotes())
                        .reportDate(LocalDateTime.now())
                        .status(Report.StatusType.PENDING)
                        .build();
                reportRepository.save(report);
                user.addReports(report);
                freeBoardComment.addReports(report);
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }

    public void reportGuestBooks(ReportRequest dto){
        CustomUserDetails customUserDetails = authenticationService.getCurrentUser();
        if(customUserDetails != null){
            User user = userAuthRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

            GuestBook guestBook = guestBookRepository.findById(dto.getGuestBookId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방명록입니다"));

            // 방명록 신고 여부 확인
            Long guestBookId = dto.getGuestBookId();
            boolean alreadyReported = user.getReports().stream()
                    .anyMatch(report -> report.getGuestBook() != null && report.getGuestBook().getId().equals(guestBookId));

            if(alreadyReported){
                throw new IllegalArgumentException("이미 신고한 방명록입니다");
            } else {
                Report report = Report.builder()
                        .reason(dto.getReason())
                        .notes(dto.getNotes())
                        .reportDate(LocalDateTime.now())
                        .status(Report.StatusType.PENDING)
                        .build();
                reportRepository.save(report);
                user.addReports(report);
                guestBook.addReports(report);
            }
        } else {
            throw new IllegalArgumentException("로그인 후 이용하세요");
        }
    }
}
