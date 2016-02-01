package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.ErrorRecord;

@Repository
public interface ErrorRepository extends JpaRepository<ErrorRecord, Long> {

}
