package fc.side.fastboard.common.file.repository;

import fc.side.fastboard.common.file.exception.FileDeleteException;
import fc.side.fastboard.common.file.exception.FileSaveException;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class FileSystemRepository {
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
}
