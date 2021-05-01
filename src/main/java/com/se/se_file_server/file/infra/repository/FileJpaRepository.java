package com.se.se_file_server.file.infra.repository;

import com.se.se_file_server.file.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<File, Long> {
  File findBySaveName(String saveName);
}
