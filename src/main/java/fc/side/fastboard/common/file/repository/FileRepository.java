package fc.side.fastboard.common.file.repository;

import fc.side.fastboard.common.file.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends CrudRepository<FileEntity, UUID> {
  Optional<FileEntity> findByFileName(UUID fileName);
  Optional<FileEntity> findByOriginFileName(String originFileName);
}
