package board.unlimited;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static javax.swing.DropMode.ON;
import static org.hibernate.FetchMode.JOIN;
import static org.hibernate.hql.internal.antlr.HqlTokenTypes.FROM;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByParentId(Long parentId);

    List<Board>  findByParentIdIsNull(Pageable pageable);

 /*   @Query(value = "WITH RECURSIVE board_tree(id, title, content, parent_id, depth, upparent) AS ( " +
            "SELECT id, title, content, parent_id, 1, id " +
            "FROM Board " +
            "WHERE parent_id IS NULL " +
            "UNION ALL " +
            "SELECT b.id, b.title, b.content, b.parent_id, bt.depth + 1, bt.upparent " +
            "FROM Board b " +
            "JOIN board_tree bt ON b.parent_id = bt.id " +
            ") " +
            "SELECT * " +
            "FROM board_tree " +
            "ORDER BY upparent desc, depth",
            nativeQuery = true)
    List<Board> findByRecursive(Pageable pageable);*/





}
