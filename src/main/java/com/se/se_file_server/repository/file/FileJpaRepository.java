package com.se.se_file_server.repository.file;

import com.se.se_file_server.domain.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<File, Long> {
  File findBySaveName(String saveName);
}
