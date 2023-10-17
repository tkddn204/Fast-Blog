package fc.side.fastboard.common.file.service;

import fc.side.fastboard.common.file.dto.*;
import fc.side.fastboard.common.file.entity.FileEntity;
import fc.side.fastboard.common.file.exception.FileDeleteException;
import fc.side.fastboard.common.file.exception.FileNotFoundException;
import fc.side.fastboard.common.file.exception.FileSaveException;
import fc.side.fastboard.common.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

  @Value("${file.dir}")
  private String FILE_DIR;

  private final FileRepository fileRepository;

  public FileResponseDTO getFile(String storedFileName) {
    return fileRepository.findByStoredFileName(storedFileName)
        .map(FileResponseDTO::fromEntity)
        .orElseThrow(FileNotFoundException::new);
  }

  @Transactional
  public FileResponseDTO saveFile(MultipartFile multipartFile) {
    String originalFileName = Objects.requireNonNull(
        multipartFile.getOriginalFilename()
    );

    // 서버에 저장될 파일명 생성 및 체크
    UUID fileUUID = UUID.randomUUID();
    String extension = getFileExtension(originalFileName);
    String storedFileName = fileUUID + "." + extension;
    String fileFullPath = getFullFilePath(storedFileName);

    saveToFileSystem(multipartFile, fileFullPath);

    FileEntity fileEntity = FileEntity.builder()
        .storedFileName(storedFileName)
        .originFileName(originalFileName)
        .filePath(fileFullPath)
        .build();
    fileRepository.save(fileEntity);

    return FileResponseDTO.fromEntity(fileEntity);
  }

  @Transactional
  public FileResponseDTO updateFile(String storedFileName, MultipartFile multipartFile) {
    String originalFileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
    Optional<FileEntity> entity = fileRepository.findByStoredFileName(storedFileName);

    // 없는 파일을 수정할 경우
    if (entity.isEmpty()) {
      // 파일 저장 후 결과 반환
      FileResponseDTO fileResponse = saveFile(multipartFile);
      FileEntity newFileEntity = FileEntity.builder()
          .storedFileName(fileResponse.storedFileName())
          .originFileName(originalFileName)
          .filePath(fileResponse.filePath())
          .build();
      return FileResponseDTO.fromEntity(newFileEntity);
    }

    // 업데이트
    // 파일 삭제 후 다시 저장
    String fileFullPath = entity.get().getFilePath();
    deleteToFileSystem(fileFullPath);
    saveToFileSystem(multipartFile, fileFullPath);
    entity.get().setOriginFileName(multipartFile.getOriginalFilename());

    return FileResponseDTO.fromEntity(entity.get());
  }

  @Transactional
  public void deleteFile(String storedFileName) {
    FileEntity fileEntity = fileRepository.findByStoredFileName(storedFileName)
        .orElseThrow(FileNotFoundException::new);

    String fileFullPath = fileEntity.getFilePath();
    deleteToFileSystem(fileFullPath);
  }

  private String getFullFilePath(String storedFileName) {
    return Path.of(FILE_DIR, storedFileName).toString();
  }

  private String getFileExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf('.') + 1);
  }

  private void deleteToFileSystem(String fileFullPath) {
    try {
      File file = new File(fileFullPath);
      if (file.exists()) {
        file.delete();
      }
    } catch (SecurityException e) {
      throw new FileDeleteException(e.getMessage());
    }
  }

  private void saveToFileSystem(MultipartFile multipartFile, String fileFullPath) {
    try {
      multipartFile.transferTo(new File(fileFullPath));
    } catch (IOException e) {
      throw new FileSaveException(e.getMessage());
    }
  }
}
