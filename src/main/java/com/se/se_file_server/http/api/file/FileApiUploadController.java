package com.se.se_file_server.http.api.file;

import com.se.se_file_server.domain.entity.file.File;
import com.se.se_file_server.domain.usecase.file.FileUploadUseCase;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "file/upload")
public class FileApiUploadController {

  private static final Logger logger = LoggerFactory.getLogger(FileApiUploadController.class);

  @Autowired
  private FileUploadUseCase fileUploadUseCase;

  @PostMapping("/uploadFile")
  public File uploadFile(@RequestParam("file") MultipartFile file) {
    String savedFileName = fileUploadUseCase.storeFile(file);

    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/file/")
        .path("/download/")
        .path(savedFileName)
        .toUriString();

    // 일단 테스트로 다운로드 Uri 넘겨줌. fileDownloadUri를 DB에 저장하고, 원할 때 보여주면 됨.
    savedFileName = fileDownloadUri;

    return new File(1L, file.getOriginalFilename(), savedFileName, file.getContentType(), file.getSize());
  }

  @PostMapping("/uploadMultipleFiles")
  public List<File> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files){
    return Arrays.asList(files)
        .stream()
        .map(file -> uploadFile(file))
        .collect(Collectors.toList());
  }

}
