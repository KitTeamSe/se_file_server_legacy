package com.se.se_file_server.domain.usecase.attachment;

import com.se.se_file_server.domain.entity.attachment.Attachment;
import com.se.se_file_server.domain.usecase.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class AttachmentUploadUseCase {

//  private final AttachmentJpaRepository attachmentRepository;

  public Attachment read(Long attachmentId){
    Optional<Attachment> attachment;
    return null;
  }


}
