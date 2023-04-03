package board.unlimited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private Long id;
    @Size(max = 20)
    @NotNull
    private String title;
    @NotNull
    @Size(min = 3, max = 300)
    private String content;
    private BoardDTO parent;
    private List<BoardDTO> children = new ArrayList<>();
    private Long depth;
    private Long upparent;


    public void setChildren(List<BoardDTO> children) {
        this.children = children;
    }


}