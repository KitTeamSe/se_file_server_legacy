package com.se.se_file_server.file.infra.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "se-file-server")
@Getter
@Setter
public class FileUploadProperties {
  private String uploadDir;
  private long maxFileSize;
}
