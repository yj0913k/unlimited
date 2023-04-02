package board.unlimited;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByParentIdIsNullOrderByChildrenAsc();

    List<Board> findAllByParentIsNull();
    List<Board> findAllByParentIdOrderByDepthAsc(Long parentId);

    List<Board> findAllByOrderByDepthAsc();

    List<Board> findByParentIsNull();


//    void deleteById(Long id);
}
