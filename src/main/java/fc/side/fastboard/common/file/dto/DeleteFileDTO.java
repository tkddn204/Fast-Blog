package fc.side.fastboard.common.file.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeleteFileDTO {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @NotNull
    private String originFileName;
  }

//  @Data
//  @Builder
//  @NoArgsConstructor
//  @AllArgsConstructor
//  public static class Response {
//    private String ;
//  }
}
