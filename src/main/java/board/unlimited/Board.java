package board.unlimited;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Board parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private List<Board> children = new ArrayList<>();
    private Long depth;
    //바로 위 부모객체의 id값
    private Long upparent;

    public BoardDTO toDTO() {
        return  BoardDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .parent(parent != null ? parent.toDTO() : null)
                .depth(depth)
                .build();

    }

}
