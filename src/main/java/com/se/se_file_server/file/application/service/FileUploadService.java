package com.se.se_file_server.file.application.service;

import com.se.se_file_server.common.domain.exception.BusinessException;
import com.se.se_file_server.config.FileUploadProperties;
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

  @Autowired
  private FileJpaRepository fileJpaRepository;

  @Autowired
  public FileUploadService(FileUploadProperties prop) {
    this.fileLocation = Paths.get(prop.getUploadDir()).toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileLocation);
    }catch(Exception e) {
      throw new BusinessException(FileUploadErrorCode.UPLOAD_PATH_DOES_NOT_EXISTS);
    }
  }

  public File storeFile(Long postId, MultipartFile file) {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    // 파일명에 부적합 문자가 있는지 확인한다.
    if(fileName.contains(".."))
      throw new BusinessException(FileUploadErrorCode.INVALID_FILE_NAME);

    if(file.getSize() < 1)
      throw new BusinessException(FileUploadErrorCode.INVALID_FILE_SIZE);

    try {
      final String extension = FilenameUtils.getExtension(file.getOriginalFilename());

      /* 서버에 저장할 파일명 (랜덤 문자열 + 확장자) */
      String randomUUID = UUID.randomUUID().toString().replaceAll("-", "");
      final String saveName = randomUUID + "." + extension;

      Path targetLocation = this.fileLocation.resolve(saveName);

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      // 파일 정보 DB에 저장
      File saveFile = new File(postId, file.getOriginalFilename(), saveName, file.getContentType(), file.getSize());

      fileJpaRepository.save(saveFile);

      return saveFile;
    }catch(Exception e) {
      throw new BusinessException(FileUploadErrorCode.UNKNOWN_UPLOAD_ERROR_CAUSED);
    }
  }
}
