package fc.side.fastboard.common.file.repository;

import fc.side.fastboard.common.file.entity.FileEntity;
import fc.side.fastboard.common.file.exception.FileDeleteException;
import fc.side.fastboard.common.file.exception.FileSaveException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Repository
public class FileSystemRepository {
    @Value("${file.dir}")
    private String FILE_DIR;

    public void saveFile(MultipartFile multipartFile, String filePath) {
        try {
            multipartFile.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new FileSaveException(e.getMessage());
        }
    }

    public void deleteFile(String filePath) {
        try {
            Files.delete(Path.of(filePath));
        } catch (IOException e) {
            throw new FileDeleteException(e.getMessage());
        }
    }

    public FileEntity createStoredFile(String originFileName) {
        UUID fileUUID = UUID.randomUUID();
        String extension = getFileExtension(originFileName);
        String storedFileName = fileUUID + "." + extension;
        return FileEntity.builder()
                .storedFileName(storedFileName)
                .originFileName(originFileName)
                .filePath(Path.of(FILE_DIR, storedFileName).toString())
                .build();
    }

    public String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
