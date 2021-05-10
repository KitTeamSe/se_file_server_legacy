package com.se.se_file_server.file.application.error;

import com.se.se_file_server.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum FileDeleteErrorCode implements ErrorCode {
  FILE_DOES_NOT_EXISTS(400, "FDE01", "파일이 존재하지 않음");

  private final String code;
  private final String message;
  private int status;

  FileDeleteErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
