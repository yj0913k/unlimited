package board.unlimited;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;

@SpringBootTest
class UnlimitedApplicationTests {


	@Autowired
	private BoardRepository boardRepository;

	@Test
	void contextLoads() {
	}

//	@Test
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-unit-name"); // persistence-unit-name 부분은 persistence.xml 파일의 name 값과 동일해야 합니다.
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin(); // 트랜잭션 시작

		em.createNativeQuery("CREATE TABLE board ("
				+ "id BIGINT(20) NOT NULL AUTO_INCREMENT, "
				+ "title VARCHAR(255) NOT NULL, "
				+ "content VARCHAR(255) NOT NULL, "
				+ "parent_id BIGINT(20), "
				+ "depth BIGINT(20), "
				+ "PRIMARY KEY (id), "
				+ "FOREIGN KEY (parent_id) REFERENCES board (id))").executeUpdate();

		em.getTransaction().commit(); // 트랜잭션 종료
		em.close();
		emf.close();
	}


	@Test
	public void create200Boards() {
		for (int i = 1; i < 200; i++) {
			Board board = Board.builder()
					.title("1")
					.content("1")
					.children(new ArrayList<>())
					.depth(0L) // 최상위 게시글이므로 깊이는 0으로 설정
					.build();
			boardRepository.save(board);
		}
	}



}
