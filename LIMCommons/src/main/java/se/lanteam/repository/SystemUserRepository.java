package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.SystemUser;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
	@Query("SELECT u FROM SystemUser u WHERE u.userName = :userName")
    public SystemUser findByUserName(@Param("userName") String userName);

}
