package com.se.se_file_server.http.api.attachment;

import com.se.se_file_server.domain.entity.attachment.Attachment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "attachment")
public class AttachmentApiController {

  @GetMapping(value = "/requestTest")
  public Attachment handleRequestTest(){
    return Attachment.createAttachment(1L, 1L, "오리지날 파일명", "저장 파일명", 300L);
  }

  @GetMapping(value = "/{postId}/upload")
  public Attachment handleFileUpload(@PathVariable("postId") Long postId){

    return Attachment.createAttachment(postId, 1L, "업로드 요청됨", "저장 파일명", 300L);
  }

  @GetMapping(value = "/{postId}/download/{attachmentId}")
  public Attachment handleFileDownload(@PathVariable("postId") Long postId, @PathVariable("attachmentId") Long attachmentId){

    return Attachment.createAttachment(postId, attachmentId, "다운로드 요청됨", "저장 파일명", 300L);
  }

  // TODO: 멀티파트 업로드/다운로드 구현
//  @GetMapping(value = "/{postId}/upload")
//  public Attachment handleFileUpload(@PathVariable("postId") Long postId, @RequestParam("file")
//      MultipartFile file){
//
//    return Attachment.createAttachment(postId, 1L, "업로드 요청됨", "저장 파일명", 300L);
//  }
//
//  @GetMapping(value = "/{postId}/download/{attachmentId}")
//  public Attachment handleFileDownload(@PathVariable("postId") Long postId, @PathVariable("attachmentId") Long attachmentId, @RequestParam("file")
//      MultipartFile file){
//
//    return Attachment.createAttachment(postId, attachmentId, "다운로드 요청됨", "저장 파일명", 300L);
//  }



}
