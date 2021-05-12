package com.se.se_file_server.file.domain.entity;


import com.se.se_file_server.common.domain.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;

@Entity
@Getter
@Table(name="attachment", indexes = {@Index(columnList = "saveName")})
public class File extends BaseEntity {

  // 첨부파일 PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  // 원본 파일명
  @Size(min = 1, max = 255)
  @Column(nullable = false)
  private String originalName;

  // 저장 파일명
  @Size(min = 1, max = 255)
  @Column(nullable = false, unique = true)
  private String saveName;

  // 파일 유형
  @Size(min = 1, max = 40)
  @Column(nullable = false)
  private String fileType;

  // 파일 크기
  @Column(nullable = false)
  private Long size;

  protected File(){}

  public File(@Size(min = 1, max = 255) String originalName,
      @Size(min = 1, max = 255)  String saveName, @Size(min = 1, max = 40) String fileType, Long size) {
    this.originalName = originalName;
    this.saveName = saveName;
    this.fileType = fileType;
    this.size = size;
  }
}
