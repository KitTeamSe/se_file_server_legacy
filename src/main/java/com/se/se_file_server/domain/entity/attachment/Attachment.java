package com.se.se_file_server.domain.entity.attachment;


import com.se.se_file_server.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Getter;

@Entity
@Getter
public class Attachment extends BaseEntity {

  // 첨부파일 PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long attachmentId;

  // 게시글 PK
  @Size(min = 5, max = 20)
  @Column
  private Long boardId;
  
  // 원본 파일명
  @Size(min = 2, max = 40)
  @Column(nullable = false)
  private String originalName;

  // 저장 파일명
  @Size(min = 2, max = 40)
  @Column(nullable = false)
  private String saveName;

  // 파일 크기
  @Column(nullable = false)
  private Long size;

  protected Attachment() {
  }

  static public Attachment createAttachment(Long attachmentId,
      @Size(min = 5, max = 20) Long boardId,
      @Size(min = 2, max = 40) String originalName,
      @Size(min = 2, max = 40) String saveName, Long size){

    Attachment attachment = new Attachment();
    attachment.attachmentId = attachmentId;
    attachment.boardId = boardId;
    attachment.originalName = originalName;
    attachment.saveName = saveName;
    attachment.size = size;

    return attachment;
  }
}
