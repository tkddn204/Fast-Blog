package fc.side.fastboard.common.file.dto;

import fc.side.fastboard.common.file.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class GetFileDTO {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {
    private String query;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private UUID fileName;
    private String originFileName;
    private String filePath;

    public static GetFileDTO.Response fromEntity(FileEntity fileEntity) {
      return Response.builder()
          .fileName(fileEntity.getFileName())
          .originFileName(fileEntity.getOriginFileName())
          .filePath(fileEntity.getFilePath())
          .build();
    }
  }
}