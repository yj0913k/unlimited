package board.unlimited;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardRepository boardRepository;

    /*
    한 화면 페이지링크 수
     */
    private static final int PAGE_POST_COUNT = 5;
    /*
    한페이지 게시글 수
     */
    private static final int BLOCK_NUM = 30;


    @Override
    public List<BoardDTO> getBoardlist(int pageNum) {
        Page<Board> page = boardRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "no")));

        List<Board> boards = page.getContent();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (Board boardEntity : boards) {
            boardDTOList.add(this.entityToDTO(boardEntity));
        }
        return boardDTOList;

    }



    /*
    게시글 등록(부모글)
     */
    @Override
    public void save(Long parentId, BoardDTO boardDTO) {
        if (parentId == null) {
//            throw new IllegalArgumentException("Parent ID must not be null");
            parentId =1L;
        }

        Board b = boardRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent Board Not Found"));

        Board board = Board.builder()
                .depth(0L)
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .parentNum(b)
                .parentId(b)
                .build();

        boardRepository.save(board);
    }


    //게시글 리스트
    @Override
    public Object getBoardList(Long no) {

        Board parentId = Board.builder().no(no).build();

        List<Board> boardResult = boardRepository.getBoardList(parentId);

        return boardResult.stream().map(this::entityToDTO).collect(Collectors.toList());
    }




    //게시글 상세페이지
    public BoardDTO detail(Long no) {
        Board board = boardRepository.findByNo(no);
        Board boardId = board;

        BoardDTO boardDTO = BoardDTO.builder()
                .title(boardId.getTitle())
                .content(boardId.getContent())
                .build();
        return boardDTO;

    }


    /*
    답글 등록
     */
    @Override
    public void saveChild(Board parentId, Long no, BoardDTO boardDTO) {
        Board board = Board.builder().parentId(parentId).build();

        Board parent = boardRepository.findByNo(no);

        Board board1 = Board.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .depth(parent.getDepth()+1)
                .parentNum(parent)
                .parentId(board)
                .build();
        boardRepository.save(board1);
    }





    @Override
    public void deleteByNo(Long no) {
        boardRepository.deleteById(no);

    }

    @Override
    public BoardDTO entityToDTO(Board boardEntity) {
        return BoardService.super.entityToDTO(boardEntity);
    }
}
