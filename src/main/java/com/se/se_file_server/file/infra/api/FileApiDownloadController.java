package com.se.se_file_server.file.infra.api;

import com.se.se_file_server.file.application.service.FileDownloadService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "file/download")
public class FileApiDownloadController {

  private static final Logger logger = LoggerFactory.getLogger(FileApiDownloadController.class);

  @Autowired
  private FileDownloadService fileDownloadService;

  @GetMapping("/{saveName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String saveName, HttpServletRequest request)
      throws UnsupportedEncodingException {
    // Load file as Resource
    Resource resource = fileDownloadService.loadFileAsResource(saveName);

    // Try to determine file's content type
    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    }
    catch (IOException ex) {
      logger.info("Could not determine file type.");
    }

    // Fallback to the default content type if type could not be determined
    if(contentType == null) {
      contentType = "application/octet-stream";
    }

    // 원본 저장명을 가져온다.
    String originalFileName = fileDownloadService.getOriginalName(saveName);

    // 한글 출력 문제 해결
    originalFileName = new String(originalFileName.getBytes(StandardCharsets.UTF_8),
        StandardCharsets.ISO_8859_1);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
        .body(resource);
  }
}
