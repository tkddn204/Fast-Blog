package fc.side.fastboard.board.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateBoardDTO {

//    @NotNull
    private Integer writerId;

    @NotBlank
    @Size(min = 3, max = 15)
    private String title;

    @NotBlank
    private String content;

    public static Board toEntity(CreateBoardDTO boardDto) {
        return Board.builder()
                .writerId(boardDto.getWriterId())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .build();
    }
}
