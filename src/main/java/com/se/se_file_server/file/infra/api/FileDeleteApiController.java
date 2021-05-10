package com.se.se_file_server.file.infra.api;

import com.se.se_file_server.common.infra.dto.SuccessResponse;
import com.se.se_file_server.file.application.service.FileDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "file/delete")
public class FileDeleteApiController {

  @Autowired
  private FileDeleteService fileDeleteService;

  @GetMapping("/{fileName:.+}")
  public SuccessResponse delete(@PathVariable String fileName){
    fileDeleteService.delete(fileName);
    return new SuccessResponse<>(HttpStatus.OK.value(), "파일 삭제에 성공했습니다.");
  }

}
