package board.unlimited;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class BoardController {


    @Autowired
    BoardRepository boardRepository;

    /*
    게시글 상세페이지 이동
 */
    @GetMapping("/{bno}")
    public String boardView(@PathVariable Long bno, BindingResult bindingResult) {

        return "boardRegister";
    }


    /*
    게시판으로 이동
     */
    @PostMapping("/board")
    public String board(@Validated @ModelAttribute BoardListForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", "에러");
        }
        model.addAttribute("board", form);
        return "board";
    }





    /*
    게시글 작성 이동
     */
    @GetMapping("/board/register")
    public String boardRegister(){
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


        return "board";
    }
}
