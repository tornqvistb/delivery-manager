package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.CustomField;

@Repository
public interface CustomFieldRepository extends JpaRepository<CustomField, Long> {
}
