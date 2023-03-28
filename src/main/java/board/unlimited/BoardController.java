package board.unlimited;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

    /*
    게시판으로 이동
     */
    @GetMapping("/board")
    public String board() {
        return "board";
    }

    /*
    게시글 상세페이지 이동
     */
    @PostMapping("/board/view")
    public String boardView() {
        return "boardView";
    }

}
