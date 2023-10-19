package fc.side.fastboard.comment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentDetailDTO {

    private Long id;
    private String writerName;
    private String writerEmail;
    private String content;
    private LocalDateTime createdAt;

    public static CommentDetailDTO fromEntity(Comment comment) {
        return CommentDetailDTO.builder()
                .id(comment.getId())
                .writerName(comment.getUser().getUsername())
                .writerEmail(comment.getUser().getEmail())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
