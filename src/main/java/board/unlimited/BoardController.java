package board.unlimited;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNum) {
        int pageSize = 5; //한 화면에 보여주는 게시글 수
        int pageBlockSize = 5; // 한번에 보여줄 페이지 수
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("id").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        List<Board> boards = boardPage.getContent();
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", boardPage.getTotalPages());

/*
현재 페이지가 중앙으로 오지 않고 5개씩 움직인다..
페이지들의 값이 고정적이기 때문에 변수를 추가해야함...
        List<Integer> pageList = IntStream.rangeClosed(center-2, center+2).boxed().collect(Collectors.toList());
*/
        int startPage = ((pageNum - 1) / pageBlockSize) * pageBlockSize + 1;
        int endPage = Math.min(startPage + pageBlockSize - 1, boardPage.getTotalPages());
        int center = startPage + (endPage - startPage) / 2;
        List<Integer> pageList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());

        Page<BoardListForm> paging = boardPage.map(BoardListForm::new);

        boolean hasPrevious = startPage > 1;
        boolean hasNext = endPage < boardPage.getTotalPages();

        startPage = center - pageBlockSize / 2;
        endPage = center + pageBlockSize / 2;
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("midPage", center);
        model.addAttribute("hasPrevious", hasPrevious);
        model.addAttribute("paging", paging);
        model.addAttribute("pageSize", Math.floor(pageSize/2));
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("pageList", pageList);
        log.info("startPage={}, endPage={}, midPage={}, hasPrevious={}, paging={}, hasNext={}, pageList={}",
                startPage, endPage, center, hasPrevious, paging, hasNext, pageList);


        List<BoardDTO> boardList = boardService.getBoardlist(pageNum);
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
