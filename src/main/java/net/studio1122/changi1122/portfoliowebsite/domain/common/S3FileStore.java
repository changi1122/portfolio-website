package net.studio1122.changi1122.portfoliowebsite.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class S3FileStore {

    @Value("${minio.endpoint}")
    @Getter
    private String endpoint;

    @Value("${minio.bucket}")
    @Getter
    private String bucket;

    private final S3Client s3Client;

    /**
     * 버킷에 파일을 업로드합니다.
     * @param file MultipartFile 객체
     * @param storeName 저장할 파일 이름 (null이면 originalFilename 사용)
     */
    public void storeFile(MultipartFile file, String storeName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String storeFilename = getStoreFilename(originalFilename, storeName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(storeFilename)
                .contentType(file.getContentType()) // MIME 타입
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()
        ));
    }

    /**
     * 버킷 내 파일 파일 목록 조회합니다.
     * @return [파일 이름(String), 파일 크기 [바이트](Long)] List
     */
    public List<Object[]> listFiles() {
        ListObjectsV2Response listRes = s3Client.listObjectsV2(b -> b.bucket(bucket));

        List<Object[]> result = new ArrayList<>();
        for (S3Object object : listRes.contents()) {
            result.add(new Object[] { object.key(), object.size() });
        }
        return result;
    }

    /**
     * 버킷 내 특정 파일을 삭제합니다.
     * @param fileName 삭제할 파일 이름
     */
    public void deleteFile(String fileName) {
        s3Client.deleteObject(b -> b.bucket(bucket).key(fileName));
    }

    private String getStoreFilename(String originalFilename, String storeName) {
        String ext = extractExt(originalFilename);
        if (StringUtils.isEmpty(storeName))
            return originalFilename;
        else
            return storeName + "." + ext;
    }

    private String extractExt(String originalFilename) {
        if (StringUtils.isEmpty(originalFilename))
            return "";

        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
