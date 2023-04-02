package board.unlimited;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public interface BoardService{
    BoardDTO detail(Long id);
    void deleteById(Long id);
    void updateById(Long id, Board board);
    List<Board> findAllBoardsSortedByDepth();

}
