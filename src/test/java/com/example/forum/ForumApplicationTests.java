package com.example.forum;

import com.example.forum.boards.freeBoard.board.entity.FreeBoard;
import com.example.forum.boards.freeBoard.board.repository.FreeBoardRepository;
import com.example.forum.user.auth.repository.UserAuthRepository;
import com.example.forum.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ForumApplicationTests {
	@Autowired
	UserAuthRepository userAuthRepository;

	@Autowired
	FreeBoardRepository freeBoardRepository;

	@Test
	public void writePosts() {
		User user =  userAuthRepository.findById(1L)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
		for(int i = 1; i < 50; i++){
			FreeBoard freeBoard = FreeBoard.builder()
					.title("테스트 " + String.valueOf(i))
					.content("asdasd")
					.createDate(LocalDateTime.now())
					.user(user)
					.view(0)
					.build();
			freeBoardRepository.save(freeBoard);
		}
	}
}
