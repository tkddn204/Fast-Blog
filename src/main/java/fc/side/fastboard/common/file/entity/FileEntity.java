package fc.side.fastboard.common.file.entity;


import fc.side.fastboard.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.nio.file.Path;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class FileEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  private String storedFileName; // 서버에 저장된 파일 이름

  @NotNull
  private String originFileName; // 사용자가 보낸 파일 이름

  @NotNull
  private String filePath;

  public static FileEntity from(String fileDirectory, String originFileName) {
    UUID fileUUID = UUID.randomUUID();
    String extension = originFileName.substring(originFileName.lastIndexOf('.') + 1);
    String storedFileName = fileUUID + "." + extension;
    return FileEntity.builder()
            .storedFileName(storedFileName)
            .originFileName(originFileName)
            .filePath(Path.of(fileDirectory, storedFileName).toString())
            .build();
  }
}
