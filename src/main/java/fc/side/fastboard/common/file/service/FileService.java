package fc.side.fastboard.common.file.service;

import fc.side.fastboard.common.file.dto.DeleteFileDTO;
import fc.side.fastboard.common.file.dto.GetFileDTO;
import fc.side.fastboard.common.file.dto.SaveFileDTO;
import fc.side.fastboard.common.file.dto.UpdateFileDTO;
import fc.side.fastboard.common.file.entity.FileEntity;
import fc.side.fastboard.common.file.exception.DuplicatedFileException;
import fc.side.fastboard.common.file.exception.FileNotFoundException;
import fc.side.fastboard.common.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

  @Value("${file.dir}")
  private String FILE_DIR;

  private final FileRepository fileRepository;

  public GetFileDTO.Response getFile(GetFileDTO.Request request) {
    String query = request.getQuery();
    // TODO: query 유효성 검사 추가
    return fileRepository.findByFileName(UUID.fromString(query))
        .map(GetFileDTO.Response::fromEntity)
        .orElseThrow(FileNotFoundException::new);
  }

  @Transactional
  public SaveFileDTO.Response saveFile(SaveFileDTO.Request request) {
    fileRepository.findByOriginFileName(request.getOriginFileName())
        .ifPresent(value -> {
          throw new DuplicatedFileException();
        });

    UUID fileName = UUID.randomUUID();
    String extension = getFileExtension(request.getOriginFileName());
    String fileFullPath = getFullFilePath(fileName, extension);

    saveToFileSystem(request.getMultipartFile(), fileFullPath);

    FileEntity fileEntity = FileEntity.builder()
        .fileName(fileName)
        .originFileName(request.getOriginFileName())
        .filePath(fileFullPath)
        .build();
    fileRepository.save(fileEntity);

    return SaveFileDTO.Response.from(fileEntity);
  }

  @Transactional
  public UpdateFileDTO.Response updateFile(UpdateFileDTO.Request request) {
    FileEntity fileEntity = fileRepository.findByOriginFileName(request.getOriginFileName())
        .orElseThrow(FileNotFoundException::new);

    String fileFullPath = fileEntity.getFilePath();

    // 파일 삭제 후 다시 저장
    deleteToFileSystem(fileFullPath);
    saveToFileSystem(request.getMultipartFile(), fileFullPath);

    return UpdateFileDTO.Response.from(fileEntity);
  }

  @Transactional
  public void deleteFile(DeleteFileDTO.Request request) {
    FileEntity fileEntity = fileRepository
        .findByFileName(UUID.fromString(request.getQuery()))
        .orElseThrow(FileNotFoundException::new);

    String fileFullPath = fileEntity.getFilePath();
    deleteToFileSystem(fileFullPath);
  }

  private String getFullFilePath(UUID fileId, String extension) {
    return Path.of(FILE_DIR, fileId.toString() + "." + extension).toString();
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
      // TODO: 파일시스템에 삭제시 예외처리 추가
      throw new RuntimeException(e);
    }
  }

  private void saveToFileSystem(MultipartFile multipartFile, String fileFullPath) {
    try {
      multipartFile.transferTo(new File(fileFullPath));
    } catch (IOException e) {
      // TODO: 파일시스템에 저장시 예외처리 추가
      throw new RuntimeException(e);
    }
  }
}
