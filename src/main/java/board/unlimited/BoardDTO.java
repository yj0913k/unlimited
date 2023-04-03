package board.unlimited;

import board.unlimited.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private BoardDTO parent;
    private List<BoardDTO> children = new ArrayList<>();
    private Long depth;
    private Long upparent;



    public void setChildren(List<BoardDTO> children) {
        this.children = children;
    }
    public void addChild(BoardDTO child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);
    }

    public void addChildren(List<BoardDTO> children) {
        for (BoardDTO child : children) {
            addChild(child);
        }
    }


}