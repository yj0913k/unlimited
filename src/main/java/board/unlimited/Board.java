package board.unlimited;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue
    private Long id;

    private Long bno;

    private String title;

    private String content;
}
