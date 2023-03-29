package board.unlimited;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public Board toEntity(){
        Board boardEntity = new Board().builder()
                .id(id)
                .title(title)
                .content(content)
                .parentNum(parentNum)
                .childNum(childNum)
                .depth(depth).build();
        return boardEntity;
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
