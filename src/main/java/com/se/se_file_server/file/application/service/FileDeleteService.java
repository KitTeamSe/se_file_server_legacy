package com.se.se_file_server.file.application.service;

import com.se.se_file_server.common.domain.exception.BusinessException;
import com.se.se_file_server.file.application.error.FileDeleteErrorCode;
import com.se.se_file_server.file.domain.entity.File;
import com.se.se_file_server.file.infra.repository.FileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileDeleteService {

  private final FileJpaRepository fileJpaRepository;

  @Transactional
  public void delete(final String fileName){
    File file = fileJpaRepository.findBySaveName(fileName).orElseThrow(() -> {
      throw new BusinessException(FileDeleteErrorCode.FILE_DOES_NOT_EXISTS);
    });

    fileJpaRepository.delete(file);
  }
}
