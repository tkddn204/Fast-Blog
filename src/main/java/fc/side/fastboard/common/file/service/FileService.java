package fc.side.fastboard.common.file.service;

import fc.side.fastboard.common.file.dto.FileResponseDTO;
import fc.side.fastboard.common.file.entity.FileEntity;
import fc.side.fastboard.common.file.exception.FileNotFoundException;
import fc.side.fastboard.common.file.repository.FileDataBaseRepository;
import fc.side.fastboard.common.file.repository.FileSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileDataBaseRepository fileDataBaseRepository;
    private final FileSystemRepository fileSystemRepository;

    @Transactional(readOnly = true)
    public FileResponseDTO getFile(String storedFileName) {
        return fileDataBaseRepository.findByStoredFileName(storedFileName)
                .map(FileResponseDTO::fromEntity)
                .orElseThrow(FileNotFoundException::new);
    }

    @Transactional
    public FileResponseDTO saveFile(MultipartFile multipartFile) {
        String originalFileName = Objects.requireNonNull(
                multipartFile.getOriginalFilename()
        );

        FileEntity newFileEntity = fileSystemRepository.createStoredFile(originalFileName);
        fileSystemRepository.saveFile(multipartFile, newFileEntity.getFilePath());
        FileEntity savedFileEntity = fileDataBaseRepository.save(newFileEntity);

        return FileResponseDTO.fromEntity(savedFileEntity);
    }

    @Transactional
    public FileResponseDTO updateFile(String storedFileName, MultipartFile multipartFile) {
        Optional<FileEntity> savedFileEntity =
                fileDataBaseRepository.findByStoredFileName(storedFileName);

        if (savedFileEntity.isEmpty()) {
            return saveFile(multipartFile);
        }

        // 파일 삭제 후 다시 저장
        FileEntity fileEntity = savedFileEntity.get();
        String fileFullPath = fileEntity.getFilePath();
        fileSystemRepository.deleteFile(fileFullPath);
        fileSystemRepository.saveFile(multipartFile, fileFullPath);
        fileEntity.setOriginFileName(multipartFile.getOriginalFilename());

        return FileResponseDTO.fromEntity(fileEntity);
    }

    @Transactional
    public void deleteFile(String storedFileName) {
        fileDataBaseRepository.findByStoredFileName(storedFileName)
                .ifPresentOrElse(fileEntity -> fileSystemRepository.deleteFile(fileEntity.getFilePath()),
                        FileNotFoundException::new);
    }
}
