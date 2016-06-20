package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
