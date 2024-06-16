package com.example.forum.boards.questionBoard.board.entity;

import com.example.forum.boards.questionBoard.comment.entity.QuestionBoardComment;
import com.example.forum.boards.questionBoard.image.entity.QuestionBoardImage;
import com.example.forum.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class QuestionBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String title;

    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int view;

    // CascadeType.REMOVE : 부모 Entity 삭제시 자식 Entity 들도 삭제
    // orphanRemoval = true : 부모 엔티티와의 관계가 끊어진 자식 엔티티들을 자동으로 삭제
    @OneToMany(mappedBy = "questionBoard", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<QuestionBoardImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "questionBoard", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<QuestionBoardComment> questionBoardComments = new ArrayList<>();

    @Builder
    public QuestionBoard(Category category, String title, String content, LocalDateTime createDate, User user, int view){
        this.category = category;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.user = user;
        this.view = view;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }

    public void addImage(QuestionBoardImage questionBoardImage){
        this.images.add(questionBoardImage);
        questionBoardImage.setBoard(this);
    }

    public void addComment(QuestionBoardComment questionBoardComment){
        this.questionBoardComments.add(questionBoardComment);
        questionBoardComment.setBoard(this);
    }

    public int incView() {
        return ++this.view;
    }

    public enum Category {
        QUESTION,
        TALK;
    }
}
