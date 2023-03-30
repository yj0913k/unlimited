package board.unlimited;


import org.springframework.ui.Model;

import java.util.List;

public interface BoardService {

    public void deleteByNo(Long no);

    default BoardDTO entityToDTO(Board boardEntity) {
        Long parentNum = boardEntity.getParentNum() == null ? null : boardEntity.getParentNum().getNo();
        Long parentId = boardEntity.getParentId() == null ? null : boardEntity.getParentId().getNo();

        return BoardDTO.builder()
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .parentNum(parentNum)
                .parentId(parentId)
                .depth(boardEntity.getDepth())
                .build();
    }

    //게시글 리스트
    Object getBoardList(Long no);

    BoardDTO detail(Long no);


    void saveChild(Board parentId, Long no, BoardDTO boardDTO);

    List<BoardDTO> getBoardlist(int pageNum);


    void save(Long parentId, BoardDTO boardDTO);
}
