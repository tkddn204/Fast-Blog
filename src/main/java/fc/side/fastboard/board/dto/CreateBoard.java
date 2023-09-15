package fc.side.fastboard.board.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class CreateBoard {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
//        @NotNull // TODO: 임시로 NotNull 해제
        private Integer writerId;

        @NotBlank
        @Size(min = 3, max = 15, message = "title size must be 3-15.")
        private String title;

        @NotBlank
        private String content;

        public static Board toEntity(Request request) {
            return Board.builder()
                    .writerId(request.getWriterId())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private Integer id;
        private Integer writerId;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private Integer hit;

        public static Response fromEntity(Board board) {
            return Response.builder()
                    .id(board.getId())
                    .writerId(board.getWriterId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdAt(board.getCreatedAt())
                    .hit(board.getHit())
                    .build();
        }
    }


}
