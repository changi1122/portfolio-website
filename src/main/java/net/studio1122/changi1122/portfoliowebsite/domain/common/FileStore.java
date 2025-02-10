package net.studio1122.changi1122.portfoliowebsite.domain.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        makeDirectoryIfNotExists(type);

        String originalFilename = file.getOriginalFilename();
        String storeFilename = getStoreFilename(originalFilename, storeName);

        File destination = new File(getFullPath(storeFilename, type));
        if (destination.exists())
            throw new IllegalArgumentException("File already exists");

        file.transferTo(destination);
    }

    public void deleteFile(String filename, UploadType type) throws IOException {
        File target = new File(getFullPath(filename, type));

        if (target.exists()) {
            boolean success = target.delete();

            if (!success)
                throw new IOException();
        }
    }

    public List<String> listFiles(UploadType type) throws IOException {
        makeDirectoryIfNotExists(type);

        String dir = getDirectory(type);
        File directory = new File(dir);

        String[] fileList = directory.list();
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
