package com.example.shop;

import com.example.shop.comment.entity.Comment;
import com.example.shop.comment.repository.CommentRepository;
import com.example.shop.qnaboard.dto.requests.QnaRequest;
import com.example.shop.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.qnaboard.repository.QnaRepository;
import com.example.shop.user.entity.User;
import com.example.shop.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
class ShopApplicationTests {
	@Autowired
	QnaRepository qnaRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CommentRepository commentRepository;

	@Test
	public void test1() { // 질문 글 생성
		User user = userRepository.findById(3L).orElse(null);

		qnaRepository.save(QuestionAndAnswer.builder()
				.title("testtesdt")
				.content("bbbbbbbbdbbb")
				.createDate(LocalDateTime.now())
				.user(user)
				.build());
	}

	@Test
	public void test2(){ // 댓글 생성
		User user = userRepository.findById(3L).orElse(null);
		QuestionAndAnswer questionAndAnswer = qnaRepository.findById(16L).orElse(null);

		commentRepository.save(Comment.builder()
				.user(user)
				.questionAndAnswer(questionAndAnswer)
				.content("asdasd")
				.createDate(LocalDateTime.now())
				.build());
	}

	@Test
	public void test3(){ // 댓글 삭제
		QuestionAndAnswer questionAndAnswer = qnaRepository.findById(Long.valueOf(16)).orElse(null);
		qnaRepository.delete(questionAndAnswer);
	}

	@Test
	public void test4(){ // 질문 글 삭제
		QuestionAndAnswer questionAndAnswer = qnaRepository.findById(Long.valueOf(16)).orElse(null);
		qnaRepository.delete(questionAndAnswer);
	}
}
