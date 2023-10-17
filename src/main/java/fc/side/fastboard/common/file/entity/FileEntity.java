package fc.side.fastboard.common.file.entity;


import fc.side.fastboard.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
}
