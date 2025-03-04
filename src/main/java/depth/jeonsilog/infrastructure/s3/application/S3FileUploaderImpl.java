package depth.jeonsilog.infrastructure.s3.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import depth.jeonsilog.infrastructure.s3.S3FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3FileUploaderImpl implements FileUploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String multipartFileUpload(MultipartFile file, String dirName) {
        if (file.isEmpty())
            return null;

        String originalFileName = file.getOriginalFilename();
        String saveFileName = S3FileUtil.createSaveFileName(originalFileName); // uuid . ext

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        String filePath = dirName + "/" + saveFileName;
        try (InputStream inputStream = file.getInputStream()) {
            // S3에 업로드
            amazonS3.putObject(new PutObjectRequest(bucket, filePath, inputStream, metadata));
        } catch (IOException e) {
            rollbackIfExists(bucket, filePath);
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }

        return S3FileUtil.getFullPath(bucket, filePath);
    }

    @Override
    public void deleteFile(String filePath, String dirName) {
        String fileName = S3FileUtil.extractFileNameFromUrl(filePath);
        if (fileName != null) {
            try {
                fileName = URLDecoder.decode(fileName, "UTF-8");
            } catch (Exception e) {
                log.error("파일명 디코딩 중 오류 발생", e);
                return;
            }
            // 디렉토리명과 파일명을 조합하여 전체 객체 키 생성
            String objectKey = dirName + "/" + fileName;

            // S3 파일 확인
            if (isS3FileExists(objectKey)) {
                amazonS3.deleteObject(bucket, objectKey);
                log.info("S3 이미지 삭제가 완료되었습니다. 파일명: {}", objectKey);
            } else {
                log.warn("S3에 해당 파일이 존재하지 않습니다. 파일명: {}", objectKey);
            }
        } else {
            log.warn("유효하지 않은 S3 이미지 URL입니다. URL: {}", filePath);
        }
    }

    // S3에 해당 파일이 존재하는지 확인
    private boolean isS3FileExists(String fileName) {
        try {
            ObjectMetadata objectMetadata = amazonS3.getObjectMetadata(bucket, fileName);
            return true;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                return false; // 파일이 존재하지 않음
            } else {
                throw e; // 다른 예외는 다시 던짐
            }
        }
    }

    private void rollbackIfExists(String bucket, String key) {
        if (amazonS3.doesObjectExist(bucket, key))
            amazonS3.deleteObject(bucket, key);
    }
}