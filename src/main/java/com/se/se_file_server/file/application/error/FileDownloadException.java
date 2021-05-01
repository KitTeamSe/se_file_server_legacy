package com.se.se_file_server.file.application.error;

public class FileDownloadException extends RuntimeException{
  public FileDownloadException(String message){
    super(message);
  }

  public FileDownloadException(String message, Throwable cause){
    super(message, cause);
  }
}