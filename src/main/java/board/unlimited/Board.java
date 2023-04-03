package board.unlimited;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "제목을 입력해주세요.")
    @Size(min=3, max = 20, message = "3~20자 입력.")
    private String title;

    @NotNull(message = "내용을 입력해주세요.")
    @Size(min = 3, max = 300, message = "3~300글자의 범위 내로 입력해주세요")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Board parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Board> children = new ArrayList<>();
    private Long depth;
    //바로 위 부모객체의 id값
    private Long upparent;


    public BoardDTO toDTO() {
        return BoardDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .parent(parent != null ? parent.toDTO() : null)
                .depth(depth)
                .build();

    }

}
