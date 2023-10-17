package fc.side.fastboard.common.file.service;

import fc.side.fastboard.common.file.dto.FileResponseDTO;
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

@ActiveProfiles("test")
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
        int fileNumber = 1;
        MockMultipartFile mockMultipartFile = createMockMultipartFile(
                TEST_FILE_NAME + fileNumber + TEST_FILE_EXT
        );

        // when
        FileResponseDTO saveResponse = fileService.saveFile(mockMultipartFile);

        // then
        File actualFile = new File(saveResponse.filePath());
        actualFile.deleteOnExit();
        assertTrue(Files.isSameFile(
                Path.of(saveResponse.filePath()), actualFile.toPath()
        ));
    }

    @DisplayName("getFile()을 호출해서 저장된 파일을 가져올 수 있다.")
    @Test
    public void shouldSuccessGetFile() throws IOException {
        // given
        int fileNumber = 2;
        MockMultipartFile mockMultipartFile = createMockMultipartFile(
                TEST_FILE_NAME + fileNumber + TEST_FILE_EXT
        );
        FileResponseDTO saveResponse = fileService.saveFile(mockMultipartFile);

        // when
        FileResponseDTO response = fileService.getFile(saveResponse.storedFileName());

        // then
        File actualFile = new File(response.filePath());
        actualFile.deleteOnExit();
        assertEquals(saveResponse.storedFileName(), response.storedFileName());
        assertEquals(saveResponse.originFileName(), response.originFileName());
        assertTrue(Files.isSameFile(
                Path.of(response.filePath()), actualFile.toPath()
        ));
    }

    @DisplayName("updateFile()을 호출해서 저장된 파일을 수정(변경)할 수 있다.")
    @Test
    public void shouldSuccessUpdateFile() throws IOException {
        // given

        // 3번 파일을 저장한다
        int fileNumber = 3;
        MockMultipartFile mockMultipartFile = createMockMultipartFile(
                TEST_FILE_NAME + fileNumber + TEST_FILE_EXT
        );
        FileResponseDTO savedFileResponse = fileService.saveFile(mockMultipartFile);
        File savedFile = new File(savedFileResponse.filePath());
        savedFile.deleteOnExit();

        // 3번 파일을 4번 파일로 수정하는 요청을 만든다
        String testUpdateFileName = TEST_FILE_NAME + 4 + TEST_FILE_EXT;
        MockMultipartFile mockUpdateMultipartFile = createMockMultipartFile(
                testUpdateFileName
        );

        // when
        FileResponseDTO fileResponse = fileService.updateFile(
                testUpdateFileName,
                mockUpdateMultipartFile
        );

        // then
        File actualFile = new File(fileResponse.filePath());
        actualFile.deleteOnExit();
        assertEquals(testUpdateFileName, fileResponse.originFileName());
        assertTrue(Files.isSameFile(
                Path.of(fileResponse.filePath()), actualFile.toPath()
        ));
    }

    @DisplayName("deleteFile()을 호출해서 저장된 파일을 삭제할 수 있다.")
    @Test
    public void shouldSuccessDeleteFile() throws IOException {
        // given

        // 5번 파일을 만든다
        int fileNumber = 5;
        MockMultipartFile mockMultipartFile = createMockMultipartFile(
                TEST_FILE_NAME + fileNumber + TEST_FILE_EXT
        );
        FileResponseDTO saveResponse = fileService.saveFile(mockMultipartFile);

        // when
        fileService.deleteFile(saveResponse.storedFileName());

        // then
        File actualFile = new File(saveResponse.filePath());
        assertFalse(actualFile.exists());
    }

    private MockMultipartFile createMockMultipartFile(String testFileName) throws IOException {
        return new MockMultipartFile(
                TEST_FILE_NAME,
                testFileName,
                TEST_FILE_CONTENT_TYPE,
                new FileInputStream(Path.of(TEST_FILE_DIR, testFileName).toFile())
        );
    }
}