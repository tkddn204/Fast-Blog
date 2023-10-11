package fc.side.fastboard.comment.entity;

import fc.side.fastboard.board.entity.Board;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    private Long writerId;

    @Column(length = 50)
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

}
