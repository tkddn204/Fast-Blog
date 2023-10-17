package fc.side.fastboard.common.file.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.common.file.entity.FileEntity;
import lombok.*;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetFileResponse(
        String storedFileName,
        String originFileName,
        String filePath
) {
    public static GetFileResponse fromEntity(FileEntity fileEntity) {
        return GetFileResponse.builder()
                .storedFileName(fileEntity.getStoredFileName())
                .originFileName(fileEntity.getOriginFileName())
                .filePath(fileEntity.getFilePath())
                .build();
    }
}
