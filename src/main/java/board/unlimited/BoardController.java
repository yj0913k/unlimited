package board.unlimited;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;


@Controller
@Slf4j
@AllArgsConstructor
public class BoardController {


    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    /*
    게시글 상세페이지 이동
 */
    @GetMapping("/{id}")
    public String boardView(@Validated @PathVariable("id") Long id, Model model) {
        BoardDTO boardDTO = boardService.detail(id);

        model.addAttribute("board", boardDTO);

        return "boardView";
    }

    //리스트 목록 버전 1
    @GetMapping("/")
    public String list(Model model, @PageableDefault(size = 10) Pageable pageable, @RequestParam(value = "page", defaultValue = "1") int pageNum) {

        //페이지 정렬 방법.
        //한 페이지당 보여줄 페이지 수
        pageable = PageRequest.of(pageNum - 1, 10, Sort.by("id").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        List<BoardDTO> boardDTOList = boardRepository.findAll(pageable).stream()
                .filter(board -> board.getParent() == null)
                .map(Board::toDTO)
                .collect(Collectors.toList());
        boardDTOList.forEach(boardDTO -> {
            List<BoardDTO> children = boardRepository.findAll().stream()
                    .filter(child -> child.getParent() != null && child.getParent().getId().equals(boardDTO.getId()))
                    .map(Board::toDTO)
                    .collect(Collectors.toList());
            boardDTO.setChildren(children);
        });

        model.addAttribute("boardDTOList", boardDTOList);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", boardPage.getTotalPages());
        int startPage = Math.max(1, boardPage.getPageable().getPageNumber() - 1);
        int endPage = Math.min(boardPage.getTotalPages(), boardPage.getPageable().getPageNumber() + 3);
        boolean hasPrevious = startPage > 1;
        boolean hasNext = endPage < boardPage.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("hasPrevious", hasPrevious);
        model.addAttribute("hasNext", hasNext);
//        List<Board> boardList = boardRepository.findAll();
//        model.addAttribute("board", boardList);



        return "board";
    }
    //리스트






    /*
    게시글 작성 이동
     */
    @GetMapping("/board/register")
    public String boardRegister() {

        return "boardRegister";
    }

    /*
    게시글 등록
    계층형 쿼리 시작  30. 10:00AM
     */

    @PostMapping("/board/register")
    public String createBoard(String title, String content) {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .children(new ArrayList<>())
                .depth(0L) // 최상위 게시글이므로 깊이는 0으로 설정
                .build();
        boardRepository.save(board);
        return "redirect:/";
    }


    /*
       답글 작성 이동
  */
    @GetMapping("/board/registerChild")
    public String boardRegisterChild(
    ) {


        return "boardRegisterChild";
    }



    //버전3
    @PostMapping("/board/registerChild")
    public String createChild(@RequestParam("parentId") Long parentId,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content) {
        Board parent = boardRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + parentId));

        Long depth = parent.getDepth() != null ? parent.getDepth() + 1 : 0;

        Board child = Board.builder()
                .title(title)
                .content(content)
                .parent(parent)
                .depth(depth)
                .build();

        parent.getChildren().add(child);

        boardRepository.save(child);
        boardRepository.save(parent);

        return "redirect:/";
    }







    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        boardService.deleteById(id);
        return "redirect:/";
    }


    @GetMapping("/board/edit/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        BoardDTO boardDTO = boardService.detail(id);

        model.addAttribute("board", boardDTO);
        return "boardEdit";
    }


    @PostMapping("/board/edit/{id}")
    public String update(@PathVariable("id") Long id, Board board) {
        boardService.updateById(id, board);
        return "redirect:/";
    }





//    private List<BoardDTO> flatten(BoardDTO boardDTO, int level) {
//        List<BoardDTO> flatList = new ArrayList<>();
//        boardDTO.setLevel(level);
//        flatList.add(boardDTO);
//        List<BoardDTO> children = boardDTO.getChildren();
//        if (children != null) {
//            for (BoardDTO child : children) {
//                flatList.addAll(flatten(child, level + 1));
//            }
//        }
//        return flatList;
//    }

}
