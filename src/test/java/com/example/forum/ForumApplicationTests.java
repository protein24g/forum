package com.example.forum;

import com.example.forum.boards.freeBoard.comment.entity.FreeBoardComment;
import com.example.forum.boards.freeBoard.comment.repository.FreeBoardCommentRepository;
import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.board.repository.FreeBoardRepository;
import com.example.forum.base.board.service.BoardService;
import com.example.forum.user.entity.User;
import com.example.forum.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ForumApplicationTests {
	@Autowired
	UserRepository userRepository;

	@Autowired
	FreeBoardRepository freeBoardRepository;

	@Autowired
	FreeBoardCommentRepository freeBoardCommentRepository;

	@Autowired
	BoardService boardService;

	@Test
	public void test0() { // 고유 id n번 유저의 글 작성
		Long n = 1L;
		User user = userRepository.findById(n).orElse(null);

		for(int i = 0; i < 100; i++){
			freeBoardRepository.save(FreeBoard.builder()
					.title("작성글 테스트" + Integer.toString(i + 1))
					.content("bbbbbbbbdbbb")
					.user(user)
					.view(0)
					.build());
		}
	}

	@Test
	public void test1(){ // 고유 id n번 유저의 댓글 작성
		User user = userRepository.findById(1L).orElse(null);
		FreeBoard freeBoard = freeBoardRepository.findById(Long.valueOf(100)).orElse(null);

		freeBoardCommentRepository.save(FreeBoardComment.builder()
			.user(user)
			.freeBoard(freeBoard)
			.content("테스트 댓글")
			.build()
		);
	}

}
