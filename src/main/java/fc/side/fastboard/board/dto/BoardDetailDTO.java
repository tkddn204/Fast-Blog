package fc.side.fastboard.board.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.comment.dto.CommentDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardDetailDTO {
    private Long id;
    private String writerName;
    private String title;
    private String content;
    private String storedFileName;
    private String originFileName;
    private List<CommentDetailDTO> comments;
    private LocalDateTime createdAt;
    private Integer hit;

  public static BoardDetailDTO fromEntity(Board board) {
    return BoardDetailDTO.builder()
            .id(board.getId())
            .writerName(board.getUser().getUsername())
            .title(board.getTitle())
            .content(board.getContent())
            .comments(
                    Optional.ofNullable(board.getComments())
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(CommentDetailDTO::fromEntity).collect(Collectors.toList())
            )
            .createdAt(board.getCreatedAt())
            .hit(board.getHit())
            .build();
  }

public static BoardDetailDTO fromEntity(Board board, String originFileName, String storedFileName) {
    return BoardDetailDTO.builder()
        .id(board.getId())
        .title(board.getTitle())
        .originFileName(originFileName)
        .storedFileName(storedFileName)
        .content(board.getContent())
        .comments(
                Optional.ofNullable(board.getComments())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(CommentDetailDTO::fromEntity).collect(Collectors.toList())
        )
        .createdAt(board.getCreatedAt())
        .hit(board.getHit())
        .build();
  }
}
