package com.se.se_file_server.common.infra.springboot.advice;

import com.se.se_file_server.common.domain.error.ErrorCode;
import com.se.se_file_server.common.domain.exception.BusinessException;
import com.se.se_file_server.common.infra.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    ErrorCode errorCode = e.getErrorCode();
    final ErrorResponse response = ErrorResponse.of(errorCode);
    e.printStackTrace();
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
  }
}
