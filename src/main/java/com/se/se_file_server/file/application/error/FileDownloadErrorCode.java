package com.se.se_file_server.file.application.error;

import com.se.se_file_server.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum FileDownloadErrorCode implements ErrorCode {

  DOWNLOAD_PATH_DOES_NOT_EXISTS(400, "FDE01", "파일을 다운로드 할 경로가 존재하지 않음"),
  FILE_DOES_NOT_EXISTS(400, "FDE02", "파일을 찾지 못함");

  private final String code;
  private final String message;
  private int status;

  FileDownloadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
