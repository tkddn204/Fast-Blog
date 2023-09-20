package fc.side.fastboard.common.file.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class FileEntity {

  @Id
  @org.hibernate.validator.constraints.UUID
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID fileId; // 파일 ID == 서버에 저장된 파일 이름

  @NotNull
  private String originFileName;

  @NotNull
  private String filePath;
}
