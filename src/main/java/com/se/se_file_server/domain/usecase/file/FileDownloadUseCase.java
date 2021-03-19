package com.se.se_file_server.domain.usecase.file;

import com.se.se_file_server.config.FileUploadProperties;
import com.se.se_file_server.domain.entity.file.File;
import com.se.se_file_server.domain.usecase.UseCase;
import com.se.se_file_server.exception.FileDownloadException;
import com.se.se_file_server.exception.FileUploadException;
import com.se.se_file_server.repository.file.FileJpaRepository;
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
  private FileJpaRepository fileJpaRepository;

  @Autowired
  public FileDownloadUseCase(FileUploadProperties prop) {
    this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();

    try {
      Files.exists(this.fileLocation);
    }catch(Exception e) {
      throw new FileUploadException("파일을 다운로드할 디렉토리를 찾지 못했습니다.", e);
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

  public String getOriginalName(String saveName){
    // DB에서 원본파일명으로 복구함.
    File file = fileJpaRepository.findBySaveName(saveName);
    return file.getOriginalName();
  }
}
