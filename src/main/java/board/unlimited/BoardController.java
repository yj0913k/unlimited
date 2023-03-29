package board.unlimited;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
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
    public String list (Model model,  @RequestParam(value = "page", defaultValue = "1") int pageNum){
        Integer[] pageList = boardService.getPageList(pageNum);

        List<BoardDTO> boardList = boardService.getBoardlist(pageNum);
        model.addAttribute("pageList", pageList);
        model.addAttribute("board", boardList);


        return "board";
    }

    /*
    게시판 이동+페이징 합체
     */
    @GetMapping("/")
    public String list(@Validated @ModelAttribute Board board , Model model,
                        @RequestParam(value = "page", defaultValue = "1") int pageNum) {

        List<BoardDTO> boardList = boardService.getBoardlist(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);
        model.addAttribute("pageList", pageList);
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
     */
    @PostMapping("/board/register")
    public String boardRegister(@Validated @ModelAttribute Board board, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", "등록 실패입니다");
        }
        board.setDepth(0L);
        board.setParentNum(0L);
        board.setChildNum(0L);

        boardRepository.save(board);



        return "redirect:/";
    }


    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        boardService.deleteById(id);
        return "redirect:/";
    }

}
