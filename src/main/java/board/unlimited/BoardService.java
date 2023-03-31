package board.unlimited;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BoardService{
    private BoardRepository boardRepository;

    /*
    한 화면 페이지링크 수
     */
    private static final int PAGE_POST_COUNT = 8;
    /*
    한페이지 게시글 수
     */
    private static final int BLOCK_NUM = 30;



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

//    board 게시글 등록 부모값 증가




    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }




}
