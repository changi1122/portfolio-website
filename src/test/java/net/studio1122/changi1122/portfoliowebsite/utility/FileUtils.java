package net.studio1122.changi1122.portfoliowebsite.utility;

import java.io.File;

public class FileUtils {

    /**
     * 특정 폴더 안의 모든 파일과 하위 폴더를 삭제합니다.
     *
     * @param folder 삭제할 폴더
     * @return 성공적으로 삭제되었는지 여부
     */
    public static boolean deleteFolderContents(File folder) {

        System.out.println("[AfterEach] Delete folder: " + folder.getAbsolutePath());

        if (folder == null || !folder.exists() || !folder.isDirectory()) {
            return false; // 유효하지 않은 폴더 처리
        }
        System.out.println("[AfterEach] 유효한 폴더 확인");

        File[] files = folder.listFiles();

        System.out.println("[AfterEach] listFiles() 실행 완료");
        
        if (files != null) {
            for (File file : files) {
                System.out.println("[AfterEach] 파일: " + file.getAbsolutePath());

                if (file.isDirectory()) {
                    // 재귀적으로 하위 폴더 삭제
                    deleteFolderContents(file);
                }
                // 파일 또는 빈 폴더 삭제
                System.out.println("[AfterEach] 파일 삭제: " + file.getAbsolutePath());
                if (!file.delete()) {
                    System.err.println("Failed to delete: " + file.getAbsolutePath());
                }
            }
        }
        return true;
    }
}
