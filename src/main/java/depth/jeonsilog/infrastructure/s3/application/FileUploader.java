package depth.jeonsilog.infrastructure.s3.application;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    String multipartFileUpload(MultipartFile file, String dirName);

    void deleteFile(String fileName, String dirName);
}

