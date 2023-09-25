package fc.side.fastboard.common.file.dto;

import fc.side.fastboard.common.file.entity.FileEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class SaveFileDTO {

  @Data
  @Builder
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @NotNull
    private String originFileName;

    @NotNull
    private MultipartFile multipartFile;
  }

  @Data
  @Builder
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private UUID fileId;
    private String originalFileName;
    private String filePath;

    public static Response from(FileEntity fileEntity) {
      return Response.builder()
          .fileId(fileEntity.getFileName())
          .originalFileName(fileEntity.getOriginFileName())
          .filePath(fileEntity.getFilePath())
          .build();
    }
  }
}
