package com.se.se_file_server.file.application.service;

import com.se.se_file_server.common.domain.exception.BusinessException;
import com.se.se_file_server.file.application.error.FileUploadErrorCode;
import com.se.se_file_server.file.infra.api.FileDownloadApiController;
import com.se.se_file_server.file.infra.config.FileUploadProperties;
import com.se.se_file_server.file.domain.entity.File;
import com.se.se_file_server.file.application.error.FileDownloadErrorCode;
import com.se.se_file_server.file.infra.repository.FileJpaRepository;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FileDownloadService {
  private final Path fileLocation;

  @Autowired
  private FileJpaRepository fileJpaRepository;

  private static final Logger logger = LoggerFactory.getLogger(FileDownloadApiController.class);

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
      logger.debug("[DL Service] Download path is " + filePath.toString());

      Resource resource = new UrlResource(filePath.toUri());

      if(!resource.exists())
        throw new BusinessException(FileDownloadErrorCode.FILE_DOES_NOT_EXISTS);

      logger.debug("[DL Service] Resource's description is " + resource.getDescription());
      logger.debug("[DL Service] Resource exists at " + resource.getFile().getAbsolutePath());
      return resource;

    }
    catch (MalformedURLException e) {
      throw new BusinessException(FileDownloadErrorCode.FILE_DOES_NOT_EXISTS);
    }
    catch (BusinessException be){
      throw be;
    }
    catch (Exception e){
      throw new BusinessException(FileDownloadErrorCode.UNKNOWN_DOWNLOAD_ERROR);
    }
  }

  public String getOriginalName(String saveName){
    // DB에서 원본파일명으로 복구함.
    File file = fileJpaRepository.findBySaveName(saveName).orElseThrow(() -> {
      throw new BusinessException(FileUploadErrorCode.FILE_DOES_NOT_EXISTS);
    });
    return file.getOriginalName();
  }
}
