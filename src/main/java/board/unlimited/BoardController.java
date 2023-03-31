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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



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
    public String boardView(@Validated @PathVariable("id") Long id,Model model) {
        BoardDTO boardDTO = boardService.detail(id);

        model.addAttribute("board", boardDTO);

        return "boardView";
    }


    /*
    게시판으로 이동
     */
  /*  @PostMapping("/board")
    public String board(@Validated @ModelAttribute Board board, BindingResult bindingResult, Model model,
                        @RequestParam(value = "page", defaultValue = "1") int pageNum) {

//        if (bindingResult.hasErrors()) {
//            log.info("errors={}", "에러");
//        }

        Integer[] pageList = boardService.getPageList(pageNum);
        model.addAttribute("pageList", pageList);


        List<BoardDTO> boardList = boardService.getBoardlist();

        model.addAttribute("board", boardList);

        return "board";
    }*/

    /*
    페이징
     */
//    @GetMapping("/page/{pageNum})")
    /*public String list (Model model,  @RequestParam(value = "page", defaultValue = "1") int pageNum){
        Integer[] pageList = boardService.getPageList(pageNum);

        List<BoardDTO> boardList = boardService.getBoardlist(pageNum);
        model.addAttribute("pageList", pageList);
        model.addAttribute("board", boardList);


        return "board";
    }*/

    /*
    한페이지에 몇개나?
     */

    /*
    게시판 이동+페이징 합체
     */
    @GetMapping("/")
    public String list(Model model,@PageableDefault(size=10) Pageable pageable, @RequestParam(value = "page", defaultValue = "1") int pageNum) {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        List<Board> boards = boardPage.getContent();
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", boardPage.getTotalPages());
        Page<BoardListDTO> paging = boardPage.map(BoardListDTO::new);

        int startPage = Math.max(1, boardPage.getPageable().getPageNumber() - 2);
        int endPage = Math.min(boardPage.getTotalPages(), boardPage.getPageable().getPageNumber() + 2);
        boolean hasPrevious = startPage > 1;
        boolean hasNext = endPage < boardPage.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("hasPrevious", hasPrevious);
        model.addAttribute("paging", paging);
        model.addAttribute("hasNext", hasNext);
        List<Board> boardList = boardRepository.findAll();
        model.addAttribute("board", boardList);


        return "board";
    }



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
    public String boardRegister(@Validated @ModelAttribute Board board, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", "등록 실패입니다");
        }
        board.setDepth(0L);
        board.setChildNum(0L);
        boardRepository.save(board);
        return "redirect:/";
    }


    /*
       답글 작성 이동
  */
    @GetMapping("/board/registerChild")
    public String boardRegisterChild(@Validated @ModelAttribute Board board,Model model) {
        Long childNum = 1L;
        model.addAttribute("childNum", childNum);
        childNum++;


        return "boardRegisterChild";
    }
/*
답글들
 */
    @PostMapping("/board/registerChild")
    public String boardRegisterChild(@Validated @ModelAttribute Board board, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", "등록 실패입니다");
        }

        boardRepository.save(board);
        return "redirect:/";
    }


    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        boardService.deleteById(id);
        return "redirect:/";
    }


}
