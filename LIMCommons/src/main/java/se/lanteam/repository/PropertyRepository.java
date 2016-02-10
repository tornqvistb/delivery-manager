package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.SystemProperty;

@Repository
public interface PropertyRepository extends JpaRepository<SystemProperty, String> {
}
