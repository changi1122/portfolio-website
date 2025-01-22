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

    public String getFullPath(String filename, boolean isImageUpload) {
        if (isImageUpload)
            return imageDir + filename;
        else
            return fileDir + filename;
    }

    public void storeFile(MultipartFile file, String storeName, boolean isImageUpload) throws IOException {
        if (file.isEmpty())
            return;

        makeDirectoryIfNotExists(isImageUpload);

        String originalFilename = file.getOriginalFilename();
        String storeFilename = getStoreFilename(originalFilename, storeName);

        File destination = new File(getFullPath(storeFilename, isImageUpload));
        if (destination.exists())
            throw new IllegalArgumentException("File already exists");

        file.transferTo(destination);
    }

    public void deleteFile(String filename, boolean isImageUpload) throws IOException {
        File target = new File(getFullPath(filename, isImageUpload));

        if (target.exists()) {
            boolean success = target.delete();

            if (!success)
                throw new IOException();
        }
    }

    public List<String> listFiles(boolean isImageUpload) throws IOException {
        makeDirectoryIfNotExists(isImageUpload);

        String dir = isImageUpload ? imageDir : fileDir;
        File directory = new File(dir);

        String[] fileList = directory.list();
        return (fileList == null) ? Collections.emptyList() : Arrays.asList(fileList);
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

}
