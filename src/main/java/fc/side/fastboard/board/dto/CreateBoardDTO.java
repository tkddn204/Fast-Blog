package fc.side.fastboard.board.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateBoardDTO implements Serializable {

//    @NotNull
    private Integer writerId;

    @NotBlank
    @Size(min = 3, max = 15)
    private String title;

    @NotBlank
    private String content;

    private MultipartFile file;

    public static Board toEntity(CreateBoardDTO boardDto) {
      return Board.builder()
          .writerId(boardDto.getWriterId())
          .title(boardDto.getTitle())
          .content(boardDto.getContent())
          .build();
    }

    public static Board toEntity(CreateBoardDTO boardDto, String fileName) {
        return Board.builder()
                .writerId(boardDto.getWriterId())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .storedFileName(fileName)
                .build();
    }
}
