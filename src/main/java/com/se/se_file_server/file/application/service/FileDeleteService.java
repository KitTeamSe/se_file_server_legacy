package com.se.se_file_server.file.application.service;

import com.se.se_file_server.common.domain.exception.BusinessException;
import com.se.se_file_server.file.application.error.FileDeleteErrorCode;
import com.se.se_file_server.file.domain.entity.File;
import com.se.se_file_server.file.infra.repository.FileJpaRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileDeleteService {

  @Autowired
  private final FileJpaRepository fileJpaRepository;

  @Autowired
  private final FileDownloadService fileDownloadService;

  @Transactional
  public void delete(final String fileName){
    File file = fileJpaRepository.findBySaveName(fileName).orElseThrow(() -> {
      throw new BusinessException(FileDeleteErrorCode.FILE_DOES_NOT_EXISTS_AT_DB);
    });

    Resource resource;
    try{
      resource = fileDownloadService.loadFileAsResource(fileName);
    }
    catch (Exception e){
      // DB상에는 파일이 있는데, 실제 파일은 없는 경우 DB 에서 삭제
      fileJpaRepository.delete(file);
      return;
    }

    try{
      java.io.File realFile = resource.getFile();

      if(!realFile.delete())
        throw new BusinessException(FileDeleteErrorCode.FAILED_TO_DELETE_FILE);

      fileJpaRepository.delete(file);
    }
    catch (IOException ioe){
      throw new BusinessException(FileDeleteErrorCode.FAILED_TO_DELETE_FILE);
    }
  }
}
