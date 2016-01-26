package se.lanteam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

	@Query("SELECT e FROM Equipment e WHERE LOWER(e.serialNo) = LOWER(:serialNo)")
    public List<Equipment> findBySerialNo(@Param("serialNo") String serialNo);

	@Query("SELECT e FROM Equipment e WHERE LOWER(e.stealingTag) = LOWER(:stealingTag)")
    public List<Equipment> findByStealingTag(@Param("stealingTag") String stealingTag);
}
