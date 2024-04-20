package com.example.shop;

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

	@Test
	public void writeTest(){
		User user = userRepository.findById(1L).orElse(null);
		if(user != null){
			for(int i = 0; i < 50; i++){
				QuestionAndAnswer questionAndAnswer = QuestionAndAnswer.builder()
						.title("테스트" + Integer.toString(i))
						.content("내용" + Integer.toString(i))
						.user(user)
						.createDate(LocalDateTime.now())
						.build();
				qnaRepository.save(questionAndAnswer);
			}

		}
	}
}
