package fc.side.fastboard.common.file.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.common.file.entity.FileEntity;
import lombok.*;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FileResponseDTO(
        String storedFileName,
        String originFileName,
        String filePath
) {
    public static FileResponseDTO fromEntity(FileEntity fileEntity) {
        return FileResponseDTO.builder()
                .storedFileName(fileEntity.getStoredFileName())
                .originFileName(fileEntity.getOriginFileName())
                .filePath(fileEntity.getFilePath())
                .build();
    }
}
