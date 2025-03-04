package depth.jeonsilog.infrastructure.s3;

import java.util.UUID;

public class S3FileUtil {

    // 파일 저장명 만들기
    public static String createSaveFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자명 구하기
    public static String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    // 전체 경로 만들기
    public static String getFullPath(String bucket, String fileName) {
        return "https://" + bucket + ".s3.amazonaws.com/" + fileName;
    }

    // url로부터 파일 이름 추출 (delete 시 사용)
    public static String extractFileNameFromUrl(String filePath) {
        try {
            return filePath.substring(filePath.lastIndexOf("/") + 1);
        } catch (Exception e) {
            return null;
        }
    }
}