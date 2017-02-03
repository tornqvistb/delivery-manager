package se.lanteam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.DeliveryArea;

@Repository
public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Long> {
	
	@Query("SELECT d FROM DeliveryArea d ORDER BY d.name")
    public List<DeliveryArea> getAllSorted();

	
}
