package fc.side.fastboard.comment.entity;

import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User user;

    @Column(length = 50)
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @CreatedDate
    private LocalDateTime createdAt;

}
