package fc.side.fastboard.common.file.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.common.file.entity.FileEntity;
import lombok.*;

public class SaveFileDTO {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String storedFileName;
        private String originalFileName;
        private String filePath;

        public static Response from(FileEntity fileEntity) {
            return Response.builder()
                    .storedFileName(fileEntity.getStoredFileName())
                    .originalFileName(fileEntity.getOriginFileName())
                    .filePath(fileEntity.getFilePath())
                    .build();
        }
    }
}
