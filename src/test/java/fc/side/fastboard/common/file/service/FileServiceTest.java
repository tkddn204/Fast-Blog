package fc.side.fastboard.common.file.service;

import fc.side.fastboard.common.file.dto.DeleteFileDTO;
import fc.side.fastboard.common.file.dto.GetFileDTO;
import fc.side.fastboard.common.file.dto.SaveFileDTO;
import fc.side.fastboard.common.file.dto.UpdateFileDTO;
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
        testFileName,
        new FileInputStream(testImageFile)
    );
    SaveFileDTO.Request request = SaveFileDTO.Request.builder()
        .originFileName(testFileName)
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
        testFileName,
        new FileInputStream(testImageFile)
    );
    SaveFileDTO.Request saveRequest = SaveFileDTO.Request.builder()
        .originFileName(testFileName)
        .multipartFile(mockMultipartFile)
        .build();
    SaveFileDTO.Response saveResponse = fileService.saveFile(saveRequest);
    String fileName = saveResponse.getFileName().toString();
    GetFileDTO.Request request = GetFileDTO.Request.builder()
        .query(fileName)
        .build();

    // when
    GetFileDTO.Response response = fileService.getFile(request);

    // then
    File actualFile = new File(response.getFilePath());
    actualFile.deleteOnExit();
    assertEquals(saveResponse.getFileName(), response.getFileName());
    assertEquals(testFileName, response.getOriginFileName());
    assertTrue(Files.isSameFile(
        Path.of(response.getFilePath()), Path.of(actualFile.getPath())
    ));
  }

  @DisplayName("updateFile()을 호출해서 저장된 파일을 수정(변경)할 수 있다.")
  @Test
  public void shouldSuccessUpdateFile() throws IOException {
    // given
    String testFileName = TEST_FILE_NAME + 3 + TEST_FILE_EXT;
    String testUpdateFileName = TEST_FILE_NAME + 4 + TEST_FILE_EXT;

    File testImageFile = Path.of(TEST_FILE_DIR, testFileName).toFile();
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        testFileName,
        new FileInputStream(testImageFile)
    );
    SaveFileDTO.Request saveRequest = SaveFileDTO.Request.builder()
        .originFileName(testFileName)
        .multipartFile(mockMultipartFile)
        .build();
    fileService.saveFile(saveRequest);

    File testUpdateImageFile = Path.of(TEST_FILE_DIR, testUpdateFileName).toFile();
    MockMultipartFile mockUpdateMultipartFile = new MockMultipartFile(
        testUpdateFileName,
        new FileInputStream(testUpdateImageFile)
    );
    UpdateFileDTO.Request updateRequest = UpdateFileDTO.Request.builder()
        .originFileName(testFileName)
        .multipartFile(mockUpdateMultipartFile)
        .build();

    // when
    UpdateFileDTO.Response response = fileService.updateFile(updateRequest);

    GetFileDTO.Request request = GetFileDTO.Request.builder()
        .query(response.getFileId().toString())
        .build();
    GetFileDTO.Response getUpdatedFileResponse = fileService.getFile(request);

    // then
    File actualFile = new File(getUpdatedFileResponse.getFilePath());
    actualFile.deleteOnExit();
    assertEquals(response.getFileId(), getUpdatedFileResponse.getFileName());
    assertEquals(testUpdateFileName, getUpdatedFileResponse.getOriginFileName());
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
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        testFileName,
        new FileInputStream(testImageFile)
    );
    SaveFileDTO.Request saveRequest = SaveFileDTO.Request.builder()
        .originFileName(testFileName)
        .multipartFile(mockMultipartFile)
        .build();
    SaveFileDTO.Response saveResponse = fileService.saveFile(saveRequest);
    String fileName = saveResponse.getFileName().toString();
    GetFileDTO.Request getRequest = GetFileDTO.Request.builder()
        .query(fileName)
        .build();
    GetFileDTO.Response getResponse = fileService.getFile(getRequest);
    DeleteFileDTO.Request request = DeleteFileDTO.Request.builder()
        .query(getResponse.getFileName().toString())
        .build();

    // when
    fileService.deleteFile(request);

    // then
    File actualFile = new File(getResponse.getFilePath());
    assertFalse(actualFile.exists());
  }
}