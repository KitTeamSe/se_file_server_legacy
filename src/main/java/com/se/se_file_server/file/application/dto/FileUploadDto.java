package com.se.se_file_server.file.application.dto;

import com.se.se_file_server.file.domain.entity.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FileUploadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class Response{

    private Long fileId;

    private String fileDownloadUrl;

    private String originalName;

    private String saveName;

    private String fileType;

    private Long size;

    static public Response fromEntity(File file){
      return Response.builder()
          .fileId(file.getId())
          .fileDownloadUrl(file.getDownloadUrl())
          .originalName(file.getOriginalName())
          .saveName(file.getSaveName())
          .fileType(file.getFileType())
          .size(file.getSize())
          .build();
    }
  }
}
