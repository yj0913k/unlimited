package board.unlimited;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class BoardListForm {

    @NotNull
    private Long no;
    @NotNull
    private String title;

    @NotNull
    private String content;

    public BoardListForm(Board b){
        this.no = b.getNo();
        this.title = b.getTitle();
        this.content = b.getContent();
    }
}
