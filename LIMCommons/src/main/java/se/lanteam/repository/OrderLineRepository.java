package se.lanteam.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.OrderLine;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

	@Query("SELECT o FROM OrderLine o WHERE o.orderHeader.id = :orderId")
    public Set<OrderLine> findByOrderId(@Param("orderId") Long orderId);

	
}
