package com.se.se_file_server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileUploadProperties {
  private String uploadDir;
  private long maxFileSize;
}
