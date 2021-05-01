package com.se.se_file_server.file.application.service;

import com.se.se_file_server.common.domain.exception.BusinessException;
import com.se.se_file_server.config.FileUploadProperties;
import com.se.se_file_server.file.domain.entity.File;
import com.se.se_file_server.file.application.error.FileDownloadErrorCode;
import com.se.se_file_server.file.infra.repository.FileJpaRepository;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class FileDownloadService {
  private final Path fileLocation;

  @Autowired
  private FileJpaRepository fileJpaRepository;

  @Autowired
  public FileDownloadService(FileUploadProperties prop) {
    this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();

    try {
      Files.exists(this.fileLocation);
    }
    catch(Exception e) {
      throw new BusinessException(FileDownloadErrorCode.DOWNLOAD_PATH_DOES_NOT_EXISTS);
    }
  }

  public Resource loadFileAsResource(String fileName) {
    try {
      Path filePath = this.fileLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if(resource.exists())
        return resource;
      else
        throw new BusinessException(FileDownloadErrorCode.FILE_DOES_NOT_EXISTS);
    }
    catch(MalformedURLException e) {
      throw new BusinessException(FileDownloadErrorCode.FILE_DOES_NOT_EXISTS);
    }
  }

  public String getOriginalName(String saveName){
    // DB에서 원본파일명으로 복구함.
    File file = fileJpaRepository.findBySaveName(saveName);
    return file.getOriginalName();
  }
}
