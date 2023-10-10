package fc.side.fastboard.board.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardDetailDTO{
  private Integer id;
  private Integer writerId;
  private String title;
  private String content;
  private String fileName;

  private LocalDateTime createdAt;
  private Integer hit;

  public static BoardDetailDTO fromEntity(Board board) {
    return BoardDetailDTO.builder()
            .id(board.getId())
            .writerId(board.getWriterId())
            .title(board.getTitle())
            .content(board.getContent())
            .createdAt(board.getCreatedAt())
            .hit(board.getHit())
            .build();
  }

  public static BoardDetailDTO fromEntity(Board board, String fileName) {
    return BoardDetailDTO.builder()
        .id(board.getId())
        .writerId(board.getWriterId())
        .title(board.getTitle())
        .fileName(fileName)
        .content(board.getContent())
        .createdAt(board.getCreatedAt())
        .hit(board.getHit())
        .build();
  }
}
