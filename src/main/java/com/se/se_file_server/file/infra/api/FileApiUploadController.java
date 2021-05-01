package com.se.se_file_server.file.infra.api;

import com.se.se_file_server.file.domain.entity.File;
import com.se.se_file_server.file.application.service.FileUploadService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "file/upload")
public class FileApiUploadController {

  @Autowired
  private FileUploadService fileUploadUseCase;

  @PostMapping("/uploadFile/{postId}")
  public String uploadFile(@PathVariable Long postId, @RequestParam("file") MultipartFile file){
    File savedFile = fileUploadUseCase.storeFile(postId, file);

    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/file/")
        .path("/download/")
        .path(savedFile.getSaveName())
        .toUriString();

    return fileDownloadUri;
  }

  @PostMapping("/uploadMultipleFiles/{postId}")
  public List<String> uploadMultipleFiles(@PathVariable Long postId, @RequestParam("files") MultipartFile[] files){
    return Arrays.asList(files)
        .stream()
        .map(file -> uploadFile(postId, file))
        .collect(Collectors.toList());
  }

}
