package com.se.se_file_server.file.infra.api;

import com.se.se_file_server.file.application.dto.FileUploadDto;
import com.se.se_file_server.file.domain.entity.File;
import com.se.se_file_server.file.application.service.FileUploadService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "file/upload")
public class FileUploadApiController {

  @Autowired
  private FileUploadService fileUploadService;

  @PostMapping("/uploadFile")
  public FileUploadDto.Response uploadFile(@RequestParam("file") MultipartFile file){
    File savedFile = fileUploadService.storeFile(file);
    return FileUploadDto.Response.fromEntity(savedFile);
  }

  @PostMapping("/uploadMultipleFiles")
  public List<FileUploadDto.Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files){
    return Arrays.asList(files)
        .stream()
        .map(file -> FileUploadDto.Response.fromEntity(fileUploadService.storeFile(file)))
        .collect(Collectors.toList());
  }

}
