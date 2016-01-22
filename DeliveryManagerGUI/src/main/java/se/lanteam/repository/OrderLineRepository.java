package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.OrderLine;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

}
