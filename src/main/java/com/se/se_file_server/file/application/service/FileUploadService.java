package com.se.se_file_server.file.application.service;

import com.se.se_file_server.common.domain.exception.BusinessException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

  private final Path fileLocation;
  private final long maxFileSize;

  @Autowired
  private FileJpaRepository fileJpaRepository;

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

      // 파일 정보 DB에 저장
      File saveFile = new File(file.getOriginalFilename(), saveName, file.getContentType(), file.getSize());

      try{
        fileJpaRepository.save(saveFile);
      }
      catch (Exception ex){
        java.io.File savedFile = new java.io.File(targetLocation.toString());
        if(savedFile.exists())
          savedFile.delete();

        throw new BusinessException(FileUploadErrorCode.DATABASE_ERROR_CAUSED);
      }


      return saveFile;
    }
    catch(Exception e) {
      throw new BusinessException(FileUploadErrorCode.UNKNOWN_UPLOAD_ERROR_CAUSED);
    }
  }

  private boolean isSameFileNameExists(Path targetLocation){
    return Files.exists(targetLocation);
  }
}
