package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.ReportsConfig;

@Repository
public interface ReportsConfigRepository extends JpaRepository<ReportsConfig, Long> {
	
}
