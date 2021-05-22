package com.se.se_file_server.file.application.service;

import com.se.se_file_server.common.domain.exception.BusinessException;
import com.se.se_file_server.file.infra.api.FileDownloadApiController;
import com.se.se_file_server.file.infra.config.FileUploadProperties;
import com.se.se_file_server.file.application.error.FileUploadErrorCode;
import com.se.se_file_server.file.domain.entity.File;
import com.se.se_file_server.file.infra.repository.FileJpaRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Transactional(readOnly = true)
public class FileUploadService {

  private final Path fileLocation;
  private final long maxFileSize;

  @Autowired
  private FileJpaRepository fileJpaRepository;

  private static final Logger logger = LoggerFactory.getLogger(FileDownloadApiController.class);

  @Autowired
  public FileUploadService(FileUploadProperties prop) {
    this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileLocation);
    }
    catch(Exception e) {
      throw new BusinessException(FileUploadErrorCode.UPLOAD_PATH_DOES_NOT_EXISTS);
    }

    maxFileSize = prop.getMaxFileSize();
  }

  @Transactional
  public File storeFile(MultipartFile file) {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    // 파일명에 부적합 문자가 있는지 확인한다.
    if(fileName.contains(".."))
      throw new BusinessException(FileUploadErrorCode.INVALID_FILE_NAME);

    // 파일 크기가 유효한지 확인한다.
    if(file.getSize() <= 0)
      throw new BusinessException(FileUploadErrorCode.INVALID_FILE_SIZE);

    if(file.getSize() >= maxFileSize)
      throw new BusinessException(FileUploadErrorCode.FILE_SIZE_LIMIT_EXCEEDED);

    try {
      final String extension = FilenameUtils.getExtension(file.getOriginalFilename());

      /* 서버에 저장할 파일명 (랜덤 문자열 + 확장자) */
      String randomUUID, saveName;
      Path targetLocation;

      do{
        randomUUID = UUID.randomUUID().toString().replaceAll("-", "");
        saveName = randomUUID + "." + extension;
        targetLocation = this.fileLocation.resolve(saveName);
      }
      while(isSameFileNameExists(targetLocation));

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      String fileDownloadUri = createDownloadUri(saveName);

      // 파일 정보 DB에 저장
      File saveFile = new File(file.getOriginalFilename(), saveName, file.getContentType(), file.getSize(), fileDownloadUri);

      try{
        fileJpaRepository.save(saveFile);
        logger.debug("[UL Service] File saved at " + targetLocation);
      }
      catch (Exception ex){
        java.io.File savedFile = new java.io.File(targetLocation.toString());
        if(savedFile.exists())
          savedFile.delete();

        logger.debug("[UL Service] Failed to save file at " + targetLocation);
        throw new BusinessException(FileUploadErrorCode.DATABASE_ERROR_CAUSED);
      }

      return saveFile;
    }
    catch(Exception e) {
      throw new BusinessException(FileUploadErrorCode.UNKNOWN_UPLOAD_ERROR);
    }
  }

  private boolean isSameFileNameExists(Path targetLocation){
    return Files.exists(targetLocation);
  }

  private String createDownloadUri(String saveName){
    return  ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/file/")
        .path("/download/")
        .path(saveName)
        .toUriString();
  }
}
