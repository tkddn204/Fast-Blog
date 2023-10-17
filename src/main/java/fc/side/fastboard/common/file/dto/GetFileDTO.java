package fc.side.fastboard.common.file.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import fc.side.fastboard.common.file.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GetFileDTO {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Request {
    private String storedFileName;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Response {

    private String storedFileName;
    private String originFileName;
    private String filePath;

    public static GetFileDTO.Response fromEntity(FileEntity fileEntity) {
      return Response.builder()
          .storedFileName(fileEntity.getStoredFileName())
          .originFileName(fileEntity.getOriginFileName())
          .filePath(fileEntity.getFilePath())
          .build();
    }
  }
}
