package board.unlimited;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Board board = (Board) o;
        return getId() != null && Objects.equals(getId(), board.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
