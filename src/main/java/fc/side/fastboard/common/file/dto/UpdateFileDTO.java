package fc.side.fastboard.common.file.dto;

import fc.side.fastboard.common.file.entity.FileEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class UpdateFileDTO {

  @Data
  @Builder
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
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private UUID fileName;
    private String filePath;

    public static Response from(FileEntity fileEntity) {
      return Response.builder()
          .fileName(fileEntity.getFileName())
          .filePath(fileEntity.getFilePath())
          .build();
    }
  }
}
