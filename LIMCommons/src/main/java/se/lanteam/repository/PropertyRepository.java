package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.SystemProperty;

@Repository
public interface PropertyRepository extends JpaRepository<SystemProperty, String> {
	
	@Query("SELECT s FROM SystemProperty s WHERE s.id = :id")
    public SystemProperty findById(@Param("id") String id);

	
}
