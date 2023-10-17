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

  public GetFileResponse getFile(String storedFileName) {
    return fileRepository.findByStoredFileName(storedFileName)
        .map(GetFileResponse::fromEntity)
        .orElseThrow(FileNotFoundException::new);
  }

  @Transactional
  public SaveFileDTO.Response saveFile(MultipartFile multipartFile) {
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

    return SaveFileDTO.Response.from(fileEntity);
  }

  @Transactional
  public UpdateFileDTO.Response updateFile(UpdateFileDTO.Request request) {
    String originalFileName = Objects.requireNonNull(
        request.getMultipartFile().getOriginalFilename()
    );
    Optional<FileEntity> entity = fileRepository.findByStoredFileName(
        request.getStoredFileName()
    );

    // 없는 파일을 수정할 경우
    if (entity.isEmpty()) {
      // 파일 저장 후 결과 반환
      SaveFileDTO.Response response = saveFile(request.getMultipartFile());
      FileEntity newFileEntity = FileEntity.builder()
          .storedFileName(response.getStoredFileName())
          .originFileName(originalFileName)
          .filePath(response.getFilePath())
          .build();
      return UpdateFileDTO.Response.from(newFileEntity);
    }

    // 업데이트
    // 파일 삭제 후 다시 저장
    String fileFullPath = entity.get().getFilePath();
    deleteToFileSystem(fileFullPath);
    saveToFileSystem(request.getMultipartFile(), fileFullPath);
    entity.get().setOriginFileName(request.getMultipartFile().getOriginalFilename());

    return UpdateFileDTO.Response.from(entity.get());
  }

  @Transactional
  public void deleteFile(DeleteFileDTO.Request request) {
    FileEntity fileEntity = fileRepository.findByStoredFileName(request.getStoredFileName())
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
