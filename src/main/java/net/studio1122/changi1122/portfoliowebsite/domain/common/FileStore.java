package net.studio1122.changi1122.portfoliowebsite.domain.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    @Value("${image.dir}")
    private String imageDir;

    @Value("${video.dir}")
    private String videoDir;

    public String getFullPath(String filename, UploadType type) {
        return switch (type) {
            case FILE -> fileDir + filename;
            case IMAGE -> imageDir + filename;
            case VIDEO -> videoDir + filename;
        };
    }

    public void storeFile(MultipartFile file, String storeName, UploadType type) throws IOException {
        if (file.isEmpty())
            return;

        log.error("디렉토리 생성");

        makeDirectoryIfNotExists(type);

        log.error("디렉토리 생성 완료");

        String originalFilename = file.getOriginalFilename();
        String storeFilename = getStoreFilename(originalFilename, storeName);

        log.error("파일 존재 여부 확인");
        
        File destination = new File(getFullPath(storeFilename, type));
        if (destination.exists())
            throw new IllegalArgumentException("File already exists");

        log.error("파일 저장");
        file.transferTo(destination);

        log.error("파일 저장 완료");
    }

    public void deleteFile(String filename, UploadType type) throws IOException {
        log.error("[삭제] 파일 객체 생성");

        File target = new File(getFullPath(filename, type));

        if (target.exists()) {
            log.error("[삭제] 파일 삭제 시도");
            boolean success = target.delete();
            log.error("[삭제] 파일 삭제 완료");

            if (!success)
                throw new IOException();
        }
    }

    public List<String> listFiles(UploadType type) throws IOException {
        makeDirectoryIfNotExists(type);

        String dir = getDirectory(type);
        File directory = new File(dir);
        log.error("[조회] directory 경로={}", dir);

        log.error("[조회] list 시작");
        String[] fileList = directory.list();

        log.error("[조회] fileList == null : {}", (fileList == null));
        log.error("[조회] fileList.length : {}", (fileList == null) ? 0 : fileList.length);
        return (fileList == null) ? Collections.emptyList() : Arrays.asList(fileList);
    }

    private void makeDirectoryIfNotExists(UploadType type) throws IOException {
        File directory = new File(getDirectory(type));
        if (!directory.exists()) {
            boolean success = directory.mkdirs();
            if (!success)
                throw new IOException();
        }
    }

    private void makeDirectoryIfNotExists(boolean isImageUpload) throws IOException {
        File directory = new File(isImageUpload ? imageDir : fileDir);
        if (!directory.exists()) {
            boolean success = directory.mkdirs();
            if (!success)
                throw new IOException();
        }
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

    private String getDirectory(UploadType type) {
        return switch (type) {
            case FILE -> fileDir;
            case IMAGE -> imageDir;
            case VIDEO -> videoDir;
        };
    }
}
