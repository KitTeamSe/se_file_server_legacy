package com.se.se_file_server.file.application.error;

import com.se.se_file_server.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum FileDownloadErrorCode implements ErrorCode {

  DOWNLOAD_PATH_DOES_NOT_EXISTS(400, "FDLE01", "파일을 다운로드 할 경로가 존재하지 않음"),
  FILE_DOES_NOT_EXISTS(400, "FDLE02", "파일이 존재하지 않음"),
  UNKNOWN_DOWNLOAD_ERROR(400, "FDLE03", "파일 다운로드 도중 알 수 없는 오류 발생");

  private final String code;
  private final String message;
  private int status;

  FileDownloadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
