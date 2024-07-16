package com.example.forum.main.report.controller;

import com.example.forum.main.report.dto.ReportRequest;
import com.example.forum.main.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportApiController {
    private final ReportService reportService;

    @PostMapping("/api/reports/users")
    public ResponseEntity<?> reportUsers(@RequestBody ReportRequest dto){
        return null;
    }

    @PostMapping("/api/reports/posts")
    public ResponseEntity<?> reportPosts(@RequestBody ReportRequest dto){
        System.out.println(dto.toString());
        try{
            reportService.reportPosts(dto);
            return ResponseEntity.status(HttpStatus.OK).body("{\"msg\" : \"신고 접수완료\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"msg\" : \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/api/reports/comments")
    public ResponseEntity<?> reportComments(@RequestBody ReportRequest dto){
        System.out.println(dto.toString());
        try{
            reportService.reportComments(dto);
            return ResponseEntity.status(HttpStatus.OK).body("{\"msg\" : \"신고 접수완료\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"msg\" : \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/api/reports/guestBooks")
    public ResponseEntity<?> reportGuestBooks(@RequestBody ReportRequest dto){
        System.out.println(dto.toString());
        try{
            reportService.reportGuestBooks(dto);
            return ResponseEntity.status(HttpStatus.OK).body("{\"msg\" : \"신고 접수완료\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"msg\" : \"" + e.getMessage() + "\"}");
        }
    }
}
