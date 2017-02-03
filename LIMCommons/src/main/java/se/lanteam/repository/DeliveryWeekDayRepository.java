package se.lanteam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.DeliveryWeekDay;

@Repository
public interface DeliveryWeekDayRepository extends JpaRepository<DeliveryWeekDay, Long> {
	
	@Query("SELECT d FROM DeliveryWeekDay d ORDER BY d.sorting")
    public List<DeliveryWeekDay> getAllSorted();
	
}
