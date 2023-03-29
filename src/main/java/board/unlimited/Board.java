package board.unlimited;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    private String title;

    @NotNull
    private String content;

    //부모글의 주소. 자신이 부모일 경우 자기 자신의 값
    @NotNull
    private Long parentNum;

    //답글의 순번
    @NotNull
    private Long childNum;

    //답글에 답글이 작성될 경우의 깊이(칸 들여쓰기가 됨)
    @NotNull
    private Long depth;




    @Builder
    public Board(Long id, String title, String content, Long parentNum, Long childNum, Long depth) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.parentNum = parentNum;
        this.childNum = childNum;
        this.depth = depth;
    }
}
