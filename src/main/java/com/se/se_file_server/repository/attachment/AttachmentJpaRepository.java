package com.se.se_file_server.repository.attachment;

import com.se.se_file_server.domain.entity.attachment.Attachment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentJpaRepository extends JpaRepository<Attachment, Long> {

}
