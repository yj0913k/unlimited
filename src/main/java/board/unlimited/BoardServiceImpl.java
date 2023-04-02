package board.unlimited;


import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;



    @Override
    public BoardDTO detail(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        Board board = optionalBoard.orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + id));

        // 하위 게시글들을 가져오기 위해 Eager Fetching 사용
        Hibernate.initialize(board.getChildren());

        List<BoardDTO> childrenDTO = new ArrayList<>();
        for (Board child : board.getChildren()) {
            childrenDTO.add(child.toDTO());
        }

        BoardDTO boardDTO = BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .parent(board.getParent() != null ? board.getParent().toDTO() : null)
                .depth(board.getDepth())
                .build();

        boardDTO.setChildren(childrenDTO);

        return boardDTO;
    }



    @Override
    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    public void updateById(Long id,Board board, Board parent) {

        Board update = boardRepository.findById(id).get();
        update.setTitle(board.getTitle());
        update.setContent(board.getContent());
        Board parentBoard = boardRepository.findById(parent.getId()).orElse(null);
        update.setParent(parentBoard.getParent());
        update.setDepth(parentBoard.getDepth());
        boardRepository.save(update);

    }

    @Override
    public List<BoardDTO> findChildren(BoardDTO boardDTO) {
        List<Board> children = boardRepository.findByParentId(boardDTO.getId());
        List<BoardDTO> result = new ArrayList<>();
        for (Board child : children) {
            BoardDTO childDTO = child.toDTO();
            childDTO.setChildren(findChildren(childDTO)); // 자식 노드의 자식 노드를 재귀적으로 찾음
            result.add(childDTO);
        }
        return result;
    }

    @Override
    public void createChild(Long id, Board board, Board parent) {
        Board parentBoard = boardRepository.findById(parent.getId()).orElse(null);
        Long depth1 = parentBoard.getDepth() + 1;
        if (parentBoard.getParent() != null) {
            parentBoard = parentBoard.getParent();
        } else {
            parent = parentBoard;
        }
        Board child = Board.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .parent(parentBoard)
                .depth(depth1)
                .build();
        boardRepository.save(child);
    }


}
