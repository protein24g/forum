package com.example.shop;

import com.example.shop.board.comment.entity.Comment;
import com.example.shop.board.comment.repository.CommentRepository;
import com.example.shop.board.freeboard.entity.Board;
import com.example.shop.board.freeboard.repository.BoardRepository;
import com.example.shop.board.qnaboard.entity.QuestionAndAnswer;
import com.example.shop.board.qnaboard.repository.QnaRepository;
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
	BoardRepository boardRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CommentRepository commentRepository;

	@Test
	public void test0() { // 글 작성 테스트
		User user = userRepository.findById(1L).orElse(null);

		for(int i = 0; i < 100; i++){
			boardRepository.save(Board.builder()
					.title("작성글 테스트" + Integer.toString(i + 1))
					.content("bbbbbbbbdbbb")
					.createDate(LocalDateTime.now())
					.user(user)
					.view(0)
					.build());
		}
	}

}
