package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.SystemUser;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
	
}
