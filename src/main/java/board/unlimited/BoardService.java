package board.unlimited;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


public class BoardService{
    private BoardRepository boardRepository;

    //entity 값을 DTO로 변환하여 리스트에 하나씩 넣어줌
    private BoardDTO dtos(Board boardEntity) {
        return BoardDTO.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .parentNum(boardEntity.getId())
                .childNum(boardEntity.getChildNum())
                .depth(boardEntity.getDepth())
                .build();
    }

    @Transactional
    public Long getBoardCount() {
        return boardRepository.count();
    }


    //게시글 상세페이지
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



    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }




}
