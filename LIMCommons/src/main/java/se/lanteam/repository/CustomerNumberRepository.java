package se.lanteam.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.CustomerNumber;

@Repository
public interface CustomerNumberRepository extends JpaRepository<CustomerNumber, Long> {
	
	@Transactional
	@Modifying
	@Query("DELETE FROM CustomerNumber c WHERE c.id > 0")
    public void deleteAllRecords();

	
}
