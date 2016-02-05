package se.lanteam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

	@Query("SELECT e FROM Email e WHERE e.status = :status")
    public List<Email> findEmailsByStatus(@Param("status") String status);

}
