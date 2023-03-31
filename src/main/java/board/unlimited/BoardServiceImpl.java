package board.unlimited;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;


    @Override
    public Long getBoardCount() {
        return boardRepository.count();    }

    @Override
    public BoardDTO detail(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        Board boardId = board.get();
        BoardDTO boardDTO = BoardDTO.builder()
                .id(boardId.getId())
                .title(boardId.getTitle())
                .content(boardId.getContent())
                .parentNum(boardId.getParentNum())
                .childNum(boardId.getChildNum())
                .depth(boardId.getDepth())
                .build();

        return boardDTO;
    }

    @Override
    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    public void updateById(Long id,Board board) {
        Board update = boardRepository.findById(id).get();
        update.setTitle(board.getTitle());
        update.setContent(board.getContent());
        boardRepository.save(board);

    }
}
