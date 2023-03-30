package board.unlimited;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b1, b2 FROM Board b1 LEFT JOIN Board b2 ON b1.no = b2.parentId.no ORDER BY b1.depth ASC, b1.no ASC")
    List<Board> getBoardList(@Param("parentId")Board parentId);


    Board findByNo(Long no);


}
