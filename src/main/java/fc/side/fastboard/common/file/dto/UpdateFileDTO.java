package fc.side.fastboard.common.file.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.common.file.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpdateFileDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        private String storedFileName;
        private String filePath;

        public static Response from(FileEntity fileEntity) {
            return Response.builder()
                    .storedFileName(fileEntity.getStoredFileName())
                    .filePath(fileEntity.getFilePath())
                    .build();
        }
    }
}
