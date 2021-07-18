package com.se.se_file_server.file.application.error;

import com.se.se_file_server.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum FileDeleteErrorCode implements ErrorCode {
  FILE_DOES_NOT_EXISTS_AT_DB(400, "FDE01", "파일 서버 DB에 파일이 존재하지 않음"),
  FAILED_TO_DELETE_FILE(400, "FDE02", "실제 파일을 삭제하는 데 실패"),
  FILE_DOES_NOT_EXISTS(400, "FDE03", "실제 파일이 존재하지 않음");

  private final String code;
  private final String message;
  private int status;

  FileDeleteErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
