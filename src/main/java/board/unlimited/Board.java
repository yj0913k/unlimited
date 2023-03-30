package board.unlimited;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long no;
    private String title;
    private String content;

    //칸 들여쓰기로 답글의 답글.. 들을 정리
    @JoinColumn
    @ManyToOne
    private Board parentNum;

    @JoinColumn
    @ManyToOne
    private Board parentId;

    private Long depth;





}
