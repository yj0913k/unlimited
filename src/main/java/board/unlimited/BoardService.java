package board.unlimited;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class BoardService {
    private BoardRepository boardRepository;

    /*
    한 화면 페이지링크 수
     */
    private static final int PAGE_POST_COUNT = 5;
    /*
    한페이지 게시글 수
     */
    private static final int BLOCK_NUM = 30;


    // 두가지를 한번에 하기에는 양이 조금 많고 추후에 무거울 수 도 있을 것 같다.
    //전체를 한번 불러 온 후에 페이징 해야하므로 분리 해야함.
    //게시글 리스트
  /*  public List<BoardDTO> getBoardlist(Integer pageNum) {

        Page<Board> page = boardRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "id")));

        List<Board> board = boardRepository.findAll();


        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (Board boardEntity : board) {
            BoardDTO boardDTO = BoardDTO.builder()
                    .id(boardEntity.getId())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .parentNum(boardEntity.getParentNum())
                    .childNum(boardEntity.getChildNum())
                    .depth(boardEntity.getDepth())
                    .build();
            boardDTOList.add(boardDTO);
        }
        return boardDTOList;


    }*/

    public List<BoardDTO> getBoardlist(Integer pageNum) {
        Page<Board> page = boardRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "id")));

        List<Board> boards = page.getContent();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (Board boardEntity : boards) {
            boardDTOList.add(this.dtos(boardEntity));
        }
        return boardDTOList;

    }

    //entity 값을 DTO로 변환하여 리스트에 하나씩 넣어줌
    private BoardDTO dtos(Board boardEntity) {
        return BoardDTO.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .parentNum(boardEntity.getParentNum())
                .childNum(boardEntity.getChildNum())
                .depth(boardEntity.getDepth())
                .build();
    }

    @Transactional
    public Long getBoardCount() {
        return boardRepository.count();
    }

    public Integer[] getPageList(Integer selectPageNum) {
        Integer[] pageList = new Integer[BLOCK_NUM];


        /*
        총 게시글
         */
        Double TotalCount = Double.valueOf(this.getBoardCount());

        /*
        마지막 페이지
         */
        int LastPageNum = (int) (Math.ceil((TotalCount / PAGE_POST_COUNT)));

        /*
        마지막 페이지(게시글 n개로 나누었을 때)
         */
        Integer blockLastPageNum = (LastPageNum > selectPageNum + BLOCK_NUM)
                ? selectPageNum + BLOCK_NUM
                : LastPageNum;
        /*
        페이지 시작번호
         */
        selectPageNum = (selectPageNum <= 3) ? 1 : selectPageNum - 2;

        for (int val = selectPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
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
