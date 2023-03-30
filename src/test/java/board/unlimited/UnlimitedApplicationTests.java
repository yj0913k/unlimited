package board.unlimited;

import org.assertj.core.api.AbstractBigIntegerAssert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class UnlimitedApplicationTests {

	@Autowired
	BoardRepository boardRepository;



	@Test
	void contextLoads() {
	}






	@Test
	void 게시글등록더미() {
		// given
		Board parentBoard = Board.builder()
				.title("부모 게시물")
				.content("부모 게시물 내용")
				.depth(0L)
				.build();
		boardRepository.save(parentBoard);

		// when
		Board childBoard = Board.builder()
				.title("자식 게시물")
				.content("자식 게시물 내용")
				.depth(1L)
				.parentId(parentBoard)
				.parentNum(parentBoard)
				.build();
		boardRepository.save(childBoard);

// then
		Board savedBoard = boardRepository.findById(childBoard.getNo())
				.orElseThrow(() -> new NoSuchElementException("게시물을 찾을 수 없습니다."));
//		assertThat(savedBoard.getTitle()).isEqualTo(childBoard.getTitle());
//		assertThat(savedBoard.getContent()).isEqualTo(childBoard.getContent());
//		assertThat(savedBoard.getParentId().getNo()).isEqualTo(parentBoard.getNo());
//		assertThat(savedBoard.getParentNum().getNo()).isEqualTo(parentBoard.getNo());
//		assertThat(savedBoard.getDepth()).isEqualTo(childBoard.getDepth());
	}


	@Test
	public void createDummyData() {
		int count = 100;

		for (int i = 0; i < count; i++) {
			Board parent = null;

			if (i > 0) {
				int randomIndex = (int) (Math.random() * i);
				parent = boardRepository.findAll().get(randomIndex);
			}

			Board board = Board.builder()
					.title("Title " + i)
					.content("Content " + i)
					.depth(parent == null ? 0L : parent.getDepth() + 1L)
					.parentNum(parent)
					.parentId(parent)
					.build();

			boardRepository.save(board);
		}
	}





}
