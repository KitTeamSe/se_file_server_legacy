package com.se.se_file_server.domain.entity.file;


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
public class File extends BaseEntity {

  // 첨부파일 PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long fileId;

  // 게시글 PK
  @Size(min = 5, max = 20)
  @Column
  private Long postId;
  
  // 원본 파일명
  @Size(min = 2, max = 40)
  @Column(nullable = false)
  private String originalName;

  // 저장 파일명
  @Size(min = 2, max = 40)
  @Column(nullable = false)
  private String saveName;

  // 파일 유형
  @Size(min = 2, max = 40)
  @Column(nullable = false)
  private String fileType;

  // 파일 크기
  @Column(nullable = false)
  private Long size;

  protected File(){}

  public File(Long postId, String originalName, String saveName, String fileType, Long size) {
    this.postId = postId;
    this.originalName = originalName;
    this.saveName = saveName;
    this.fileType = fileType;
    this.size = size;
  }

  public static File createFile(
      @Size(min = 5, max = 20) Long postId,
      @Size(min = 2, max = 40) String originalName,
      @Size(min = 2, max = 40) String saveName,
      @Size(min = 2, max = 40) String fileType,
      Long size){

    File file = new File();
    file.postId = postId;
    file.originalName = originalName;
    file.saveName = saveName;
    file.fileType = fileType;
    file.size = size;

    return file;
  }
}
