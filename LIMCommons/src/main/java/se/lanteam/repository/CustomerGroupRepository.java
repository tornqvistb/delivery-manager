package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.CustomerGroup;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {
	@Query("SELECT c FROM CustomerGroup c WHERE c.id = :id")
    public CustomerGroup findById(@Param("id") Long id);
}
