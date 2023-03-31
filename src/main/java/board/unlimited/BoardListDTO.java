package board.unlimited;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class BoardListDTO {

    @NotNull
    private Long id;
    @NotNull
    private String title;



    public BoardListDTO(Board b){
        this.id = b.getId();
        this.title = b.getTitle();

    }
}
