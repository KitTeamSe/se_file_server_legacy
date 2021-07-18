package com.se.se_file_server.file.application.error;

import com.se.se_file_server.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum FileUploadErrorCode implements ErrorCode {

  UPLOAD_PATH_DOES_NOT_EXISTS(400, "FULE01", "업로드할 경로가 존재하지 않음"),
  INVALID_FILE_NAME(400, "FULE02", "유효하지 않은 파일명"),
  INVALID_FILE_SIZE(400, "FULE03", "유효하지 않은 파일 크기"),
  FILE_SIZE_LIMIT_EXCEEDED(400, "FULE04", "파일 용량 초과"),
  UNKNOWN_UPLOAD_ERROR(400, "FULE05", "업로드 도중 알 수 없는 오류 발생"),
  DATABASE_ERROR_CAUSED(400, "FULE06", "업로드 도중 데이터베이스에서 오류 발생"),
  FILE_DOES_NOT_EXISTS(400, "FULE07", "파일이 존재하지 않음");

  private final String code;
  private final String message;
  private int status;

  FileUploadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
