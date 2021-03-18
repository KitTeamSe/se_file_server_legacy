package com.se.se_file_server.domain.usecase.file;

import com.se.exception.FileDownloadException;
import com.se.exception.FileUploadException;
import com.se.se_file_server.config.FileUploadProperties;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadDownloadService {
  private final Path fileLocation;

  @Autowired
  public FileUploadDownloadService(FileUploadProperties prop){
    this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileLocation);
    }catch(Exception e) {
      throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
    }
  }

  public String storeFile(MultipartFile file) {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    try {
      // 파일명에 부적합 문자가 있는지 확인한다.
      if(fileName.contains(".."))
        throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);

      if(file.getSize() < 1)
        throw new FileUploadException("유효하지 않은 파일 크기입니다. " + fileName);

      final String extension = FilenameUtils.getExtension(file.getOriginalFilename());

      /* 서버에 저장할 파일명 (랜덤 문자열 + 확장자) */
      String randomUUID = UUID.randomUUID().toString().replaceAll("-", "");
      final String saveName = randomUUID + "." + extension;

      Path targetLocation = this.fileLocation.resolve(saveName);

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return saveName;
    }catch(Exception e) {
      throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
    }
  }

  public Resource loadFileAsResource(String fileName) {
    try {
      Path filePath = this.fileLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if(resource.exists()) {
        return resource;
      }else {
        throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.");
      }
    }catch(MalformedURLException e) {
      throw new FileDownloadException(fileName + " 파일을 찾을 수 없습니다.", e);
    }
  }


}
