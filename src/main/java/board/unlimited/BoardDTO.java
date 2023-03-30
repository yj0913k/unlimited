package board.unlimited;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private Long parentNum;
    private Long parentId;
    private Long depth;
}
