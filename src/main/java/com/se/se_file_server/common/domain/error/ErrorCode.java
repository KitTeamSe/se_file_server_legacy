package com.se.se_file_server.common.domain.error;

public interface ErrorCode {

  String getCode();

  String getMessage();

  int getStatus();

}