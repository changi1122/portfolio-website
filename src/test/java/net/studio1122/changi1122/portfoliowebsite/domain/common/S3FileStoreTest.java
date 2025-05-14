package net.studio1122.changi1122.portfoliowebsite.domain.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3FileStoreTest {

    @Autowired
    S3FileStore s3FileStore;

    @DisplayName("버킷에 파일을 업로드합니다.")
    @Test
    void storeFile() throws IOException {
        // given
        MultipartFile uploadFile = getUploadFile();

        // when
        s3FileStore.storeFile(uploadFile, "store");

        // then
        s3FileStore.deleteFile("store.jpg");
    }

    @DisplayName("버킷의 파일 목록을 조회합니다.")
    @Test
    void listFiles() throws IOException {
        // given
        MultipartFile uploadFile = getUploadFile();
        s3FileStore.storeFile(uploadFile, null);
        s3FileStore.storeFile(uploadFile, "test");

        // when
        List<Object[]> fileList = s3FileStore.listFiles();

        // then
        List<String> filenames = fileList.stream().map(o -> (String) o[0]).toList();
        assertThat(filenames).contains("sky.jpg", "test.jpg");

        s3FileStore.deleteFile("sky.jpg");
        s3FileStore.deleteFile("test.jpg");
    }

    @DisplayName("버킷 내 특정 파일을 삭제합니다.")
    @Test
    void deleteFile() throws IOException {
        // given
        MultipartFile uploadFile = getUploadFile();
        s3FileStore.storeFile(uploadFile, "store");

        // when
        s3FileStore.deleteFile("store.jpg");

        // then
        List<Object[]> fileList = s3FileStore.listFiles();
        List<String> filenames = fileList.stream().map(o -> (String) o[0]).toList();
        assertThat(filenames).doesNotContain("store.jpg");
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