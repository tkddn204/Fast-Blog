package fc.side.fastboard.board.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateBoardDTO implements Serializable {

    @NotBlank
    @Size(min = 3, max = 15)
    private String title;

    @NotBlank
    private String content;

    private MultipartFile file;

    public static Board toEntity(CreateBoardDTO boardDto, User user) {
      return Board.builder()
              .user(user)
              .title(boardDto.getTitle())
              .content(boardDto.getContent())
              .build();
    }

    public static Board toEntity(CreateBoardDTO boardDto, UUID fileId, User user) {
        return Board.builder()
                .user(user)
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .fileId(fileId)
                .build();
    }
}
