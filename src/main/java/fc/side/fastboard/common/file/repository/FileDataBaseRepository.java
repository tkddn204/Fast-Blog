package fc.side.fastboard.common.file.repository;

import fc.side.fastboard.common.file.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FileDataBaseRepository extends CrudRepository<FileEntity, Integer> {
  Optional<FileEntity> findByStoredFileName(String storedFileName);
}
