package net.studio1122.changi1122.portfoliowebsite.domain.common;

import net.studio1122.changi1122.portfoliowebsite.utility.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileStoreTest {

    @Autowired
    FileStore fileStore;

    @Value("${file.dir}")
    private String fileDir;
    @Value("${image.dir}")
    private String imageDir;

    @AfterEach
    void tearDown() {
        FileUtils.deleteFolderContents(new File(fileDir));
        FileUtils.deleteFolderContents(new File(imageDir));
    }

    @DisplayName("업로드한 파일을 파일 디렉터리에 저장합니다.")
    @CsvSource(value = {"null, false", "newName, true"}, nullValues = { "null" })
    @ParameterizedTest
    void storeFile(String storeName, Boolean isImageUpload) throws IOException {
        // given
        MultipartFile uploadFile = getUploadFile();

        // when
        fileStore.storeFile(uploadFile, storeName, isImageUpload);

        // then
        String dir = (isImageUpload) ? imageDir : fileDir;
        String filename = (storeName == null) ? "sky.jpg" : storeName + ".jpg";
        File file = new File(dir + filename);
        assertThat(file).exists();
    }

    @DisplayName("업로드한 파일을 파일 디렉터리에서 삭제합니다.")
    @CsvSource({"false", "true"})
    @ParameterizedTest
    void deleteFile(Boolean isImageUpload) throws IOException {
        // given
        MultipartFile uploadFile = getUploadFile();
        fileStore.storeFile(uploadFile, null, isImageUpload);

        // when
        fileStore.deleteFile("sky.jpg", isImageUpload);

        // then
        String dir = (isImageUpload) ? imageDir : fileDir;
        File file = new File(dir + "sky.jpg");
        assertThat(file).doesNotExist();
    }

    private MockMultipartFile getUploadFile() throws IOException {
        final String filename = "sky.jpg";
        final String filePath = "src/test/resources/" + filename;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        return new MockMultipartFile(
                "images",
                filename,
                "image/jpeg",
                fileInputStream
        );
    }

}