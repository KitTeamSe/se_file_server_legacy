package com.se.se_file_server.domain.usecase.file;

import com.se.se_file_server.config.FileUploadProperties;
import com.se.se_file_server.domain.usecase.UseCase;
import com.se.se_file_server.exception.FileDownloadException;
import com.se.se_file_server.exception.FileUploadException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@UseCase
public class FileDownloadUseCase {
  private final Path fileLocation;

  @Autowired
  public FileDownloadUseCase(FileUploadProperties prop) {
    this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileLocation);
    }catch(Exception e) {
      throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
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
