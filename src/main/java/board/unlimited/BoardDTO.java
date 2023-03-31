package board.unlimited;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@ToString
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private Long parentNum;

    private Long childNum;
    private Long depth;


    public Board toDTO() {
        Board board = BoardDTO.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .parentNum(this.parentNum)
                .childNum(this.childNum)
                .depth(this.depth)
                .build().toDTO();
        return board;
    }

    @Builder
    public BoardDTO(Long id, String title, String content, Long parentNum, Long childNum, Long depth) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.parentNum = parentNum;
        this.childNum = childNum;
        this.depth = depth;
    }
}
