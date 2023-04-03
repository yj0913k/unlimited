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
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
   게시글 작성 이동
    */
    @GetMapping("/board/register")
    public String boardRegister(Model model) {
        model.addAttribute("board", new Board());
        return "boardRegister";
    }

    /*
    게시글 등록
    계층형 쿼리 시작  30. 10:00AM
     */

    @PostMapping("/board/register")
    public String createBoard(@Validated @ModelAttribute Board board, BindingResult bindingResult) {
        /*
         *게시글 등록. 최상위 계층이며 children 에 배열 생성.
         * 깊이는 0
         */
        if(board.getTitle() ==null ||board.getTitle().length()>20){
            bindingResult.addError(new FieldError("board", "title", "1~20 입력요망"));
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "boardRegister";
        }

        Board board1 = Board.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .children(new ArrayList<>())
                .depth(0L) // 최상위 게시글이므로 깊이는 0으로 설정
                .build();
        boardRepository.save(board1);
        return "redirect:/";
    }

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
        List<BoardDTO> boardDTOList = new ArrayList<>();
        //계층형 게시판 구현 쿼리문ING
//        List<Board> boardList = boardRepository.findByRecursive(pageable);
        //최상위 계층객체들을 찾아서 리스트에 넣어줌
        List<Board> boardList = boardRepository.findByParentIdIsNull(pageable);
        boardList.forEach(board -> {
            BoardDTO boardDTO = board.toDTO();
            boardDTO.setChildren(boardService.findChildren(boardDTO));
            boardDTOList.add(boardDTO);
        });
        //모든 객체 불러오기
        boardDTOList.forEach(boardDTO -> {
            List<BoardDTO> children = boardRepository.findAll().stream()
                    .filter(child -> child.getParent() != null && child.getParent().getId().equals(boardDTO.getId()))
                    .map(Board::toDTO)
                    .collect(Collectors.toList());
            boardDTO.setChildren(children);
        });
        //getTotalPage의 경우 기본값으로 id를 잡으면 삭제시 페이징처리에 문제 되므로
        //getTotalElemenths로 설정함
        System.out.println(boardPage.getTotalElements());
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("boardDTOList", boardDTOList);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", boardPage.getTotalElements()%10!=0? boardPage.getTotalElements()/10+1: boardPage.getTotalElements()/10);
        int startPage = Math.max(1, boardPage.getPageable().getPageNumber() - 1);
        int lastPage = (int) (boardPage.getTotalElements() % 10 != 0 ? boardPage.getTotalElements() / 10 + 1 : boardPage.getTotalElements() / 10);
        int endPage = Math.min(lastPage, boardPage.getPageable().getPageNumber() + 3);
        boolean hasPrevious = startPage > 1;
        boolean hasNext = endPage < lastPage;
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("hasPrevious", hasPrevious);
        model.addAttribute("hasNext", hasNext);
        return "board";
    }
    //리스트







    /*
       답글 작성 이동
  */
    @GetMapping("/board/registerChild/{id}")
    public String boardRegisterChild( @PathVariable("id") Long id, Model model) {
            BoardDTO boardDTO = boardService.detail(id);
            model.addAttribute("board", boardDTO);
        return "boardRegisterChild";
    }


    //버전3
    @PostMapping("/board/registerChild/{id}")
    public String createChild(@PathVariable("id") Long id, Board board, Board parent) {
        boardService.createChild(id, board, parent);
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
    public String update(@PathVariable("id") Long id, Board board, Board parent) {
        boardService.updateById(id, board, parent);
        return "redirect:/";
    }


}
