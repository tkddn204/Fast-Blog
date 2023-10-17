package fc.side.fastboard.common.file.service;

import fc.side.fastboard.common.file.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileServiceTest {

  private static final String TEST_FILE_NAME = "test_image_";
  private static final String TEST_FILE_EXT = ".png";
  private static final String TEST_FILE_CONTENT_TYPE = "image/png";

  private static String TEST_FILE_DIR;

  @Value("${file.test-dir}")
  public void setNameStatic(String testDir) {
    FileServiceTest.TEST_FILE_DIR = testDir;
  }

  @Autowired
  private FileService fileService;

  @DisplayName("saveFile()을 호출해서 파일을 저장할 수 있다.")
  @Test
  public void shouldSuccessSaveFile() throws IOException {
    // given
    String testFileName = TEST_FILE_NAME + 1 + TEST_FILE_EXT;
    File testImageFile = Path.of(TEST_FILE_DIR, testFileName).toFile();
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        TEST_FILE_NAME,
        testFileName,
        TEST_FILE_CONTENT_TYPE,
        new FileInputStream(testImageFile)
    );
    SaveFileDTO.Request request = SaveFileDTO.Request.builder()
        .multipartFile(mockMultipartFile)
        .build();

    // when
    SaveFileDTO.Response response = fileService.saveFile(request);

    // then
    File actualFile = new File(response.getFilePath());
    actualFile.deleteOnExit();
    assertTrue(Files.isSameFile(
        Path.of(response.getFilePath()), Path.of(actualFile.getPath())
    ));
  }

  @DisplayName("getFile()을 호출해서 저장된 파일을 가져올 수 있다.")
  @Test
  public void shouldSuccessGetFile() throws IOException {
    // given
    String testFileName = TEST_FILE_NAME + 2 + TEST_FILE_EXT;
    File testImageFile = Path.of(TEST_FILE_DIR, testFileName).toFile();
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        TEST_FILE_NAME,
        testFileName,
        TEST_FILE_CONTENT_TYPE,
        new FileInputStream(testImageFile)
    );
    SaveFileDTO.Request saveRequest = SaveFileDTO.Request.builder()
        .multipartFile(mockMultipartFile)
        .build();
    SaveFileDTO.Response saveResponse = fileService.saveFile(saveRequest);

    // when
    GetFileResponse response = fileService.getFile(saveResponse.getStoredFileName());

    // then
    File actualFile = new File(response.filePath());
    actualFile.deleteOnExit();
    assertEquals(saveResponse.getStoredFileName(), response.storedFileName());
    assertEquals(testFileName, response.originFileName());
    assertTrue(Files.isSameFile(
        Path.of(response.filePath()), Path.of(actualFile.getPath())
    ));
  }

  @DisplayName("updateFile()을 호출해서 저장된 파일을 수정(변경)할 수 있다.")
  @Test
  public void shouldSuccessUpdateFile() throws IOException {
    // given
    String testFileName = TEST_FILE_NAME + 3 + TEST_FILE_EXT;
    String testUpdateFileName = TEST_FILE_NAME + 4 + TEST_FILE_EXT;
    
    // 3번 파일을 저장한다
    File testImageFile = Path.of(TEST_FILE_DIR, testFileName).toFile();
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        TEST_FILE_NAME,
        testFileName,
        TEST_FILE_CONTENT_TYPE,
        new FileInputStream(testImageFile)
    );
    SaveFileDTO.Request saveRequest = SaveFileDTO.Request.builder()
        .multipartFile(mockMultipartFile)
        .build();
    fileService.saveFile(saveRequest);

    // 3번 파일을 수정하는 요청을 만든다
    File testUpdateImageFile = Path.of(TEST_FILE_DIR, testUpdateFileName).toFile();
    MockMultipartFile mockUpdateMultipartFile = new MockMultipartFile(
        TEST_FILE_NAME,
        testUpdateFileName,
        TEST_FILE_CONTENT_TYPE,
        new FileInputStream(testUpdateImageFile)
    );
    UpdateFileDTO.Request updateRequest = UpdateFileDTO.Request.builder()
        .multipartFile(mockUpdateMultipartFile)
        .build();

    // when
    UpdateFileDTO.Response response = fileService.updateFile(updateRequest);
    GetFileResponse getUpdatedFileResponse = fileService.getFile(response.getStoredFileName());

    // then
    File actualFile = new File(getUpdatedFileResponse.filePath());
    actualFile.deleteOnExit();
    assertEquals(response.getStoredFileName(), getUpdatedFileResponse.storedFileName());
    assertEquals(testUpdateFileName, getUpdatedFileResponse.originFileName());
    assertTrue(Files.isSameFile(
        Path.of(response.getFilePath()), Path.of(actualFile.getPath())
    ));
  }

  @DisplayName("deleteFile()을 호출해서 저장된 파일을 삭제할 수 있다.")
  @Test
  public void shouldSuccessDeleteFile() throws IOException {
    // given
    String testFileName = TEST_FILE_NAME + 5 + TEST_FILE_EXT;
    File testImageFile = Path.of(TEST_FILE_DIR, testFileName).toFile();

    // 5번 파일을 만든다
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        TEST_FILE_NAME,
        testFileName,
        TEST_FILE_CONTENT_TYPE,
        new FileInputStream(testImageFile)
    );
    SaveFileDTO.Request saveRequest = SaveFileDTO.Request.builder()
        .multipartFile(mockMultipartFile)
        .build();
    SaveFileDTO.Response saveResponse = fileService.saveFile(saveRequest);

    // 5번 파일의 storedFileName을 받아온다
    String fileName = saveResponse.getStoredFileName();
    DeleteFileDTO.Request request = DeleteFileDTO.Request.builder()
        .storedFileName(fileName)
        .build();

    // when
    fileService.deleteFile(request);

    // then
    File actualFile = new File(fileName);
    assertFalse(actualFile.exists());
  }
}