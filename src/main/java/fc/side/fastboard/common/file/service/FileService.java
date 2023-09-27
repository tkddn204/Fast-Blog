package fc.side.fastboard.common.file.service;

import fc.side.fastboard.common.exception.InvalidParamException;
import fc.side.fastboard.common.file.dto.DeleteFileDTO;
import fc.side.fastboard.common.file.dto.GetFileDTO;
import fc.side.fastboard.common.file.dto.SaveFileDTO;
import fc.side.fastboard.common.file.dto.UpdateFileDTO;
import fc.side.fastboard.common.file.entity.FileEntity;
import fc.side.fastboard.common.file.exception.DuplicatedFileException;
import fc.side.fastboard.common.file.exception.FileDeleteException;
import fc.side.fastboard.common.file.exception.FileNotFoundException;
import fc.side.fastboard.common.file.exception.FileSaveException;
import fc.side.fastboard.common.file.repository.FileRepository;
import fc.side.fastboard.common.file.util.QueryCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

  @Value("${file.dir}")
  private String FILE_DIR;

  private final FileRepository fileRepository;

  public GetFileDTO.Response getFile(GetFileDTO.Request request) {
    String query = URI.create(request.getQuery()).toString();
    if (query == null || query.isEmpty()) {
      throw new InvalidParamException();
    }

    return switch (QueryCheck.check(query)) {
      case UUID_TYPE -> fileRepository.findByFileName(UUID.fromString(query))
            .map(GetFileDTO.Response::fromEntity)
            .orElseThrow(FileNotFoundException::new);
      case ORIGIN_FILE_NAME_TYPE -> fileRepository.findByOriginFileName(query)
          .map(GetFileDTO.Response::fromEntity)
          .orElseThrow(FileNotFoundException::new);
    };
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
    Optional<FileEntity> entity = fileRepository.findByFileName(request.getFileId());

    // 업데이트 전에 파일이 없었을 경우
    if (entity.isEmpty()) {
      SaveFileDTO.Response response = saveFile(SaveFileDTO.Request.builder()
          .originFileName(request.getOriginFileName())
          .multipartFile(request.getMultipartFile())
          .build()
      );
      FileEntity newFileEntity = FileEntity.builder()
          .fileName(response.getFileId())
          .originFileName(request.getOriginFileName())
          .filePath(response.getFilePath())
          .build();
      return UpdateFileDTO.Response.from(newFileEntity);
    }

    String fileFullPath = entity.get().getFilePath();

    // 파일 삭제 후 다시 저장
    deleteToFileSystem(fileFullPath);
    saveToFileSystem(request.getMultipartFile(), fileFullPath);
    entity.get().setOriginFileName(request.getMultipartFile().getName());

    return UpdateFileDTO.Response.from(entity.get());
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
