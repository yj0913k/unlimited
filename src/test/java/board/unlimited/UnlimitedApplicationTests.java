package board.unlimited;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UnlimitedApplicationTests {

	@Autowired
	BoardRepository boardRepository;



	@Test
	void contextLoads() {
	}





	@Test
	void 게시글등록() {

		Board board = new Board(0L, 0L, "title", "content", 0L, 0L, 0L);
		Board board1 = new Board(1L, 1L, "title", "content", 1L, 1L, 1L);

		boardRepository.save(board);
		boardRepository.save(board1);


		/*board.setId(1LL);
		board.setBno(1LL);
		board.setTitle("제목");
		board.setContent("내용");
		board.setParentNum(1LL);
		board.setChildNum(0L);
		board.setDepth(0L);
		boardRepository.save(board);*/

	}

}
