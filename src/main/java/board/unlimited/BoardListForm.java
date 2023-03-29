package board.unlimited;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class BoardListForm {

    @NotNull
    private Long id;
    @NotNull
    private String title;

    @NotNull
    private String content;

    public BoardListForm(Board b){
        this.id = b.getId();
        this.title = b.getTitle();
        this.content = b.getContent();
    }
}
