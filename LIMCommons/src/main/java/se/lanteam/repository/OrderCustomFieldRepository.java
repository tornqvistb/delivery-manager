package se.lanteam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.OrderCustomField;

@Repository
public interface OrderCustomFieldRepository extends JpaRepository<OrderCustomField, Long> {
	@Query("SELECT o FROM OrderCustomField o WHERE o.orderHeader.id = :orderId")
    public List<OrderCustomField> findByOrderId(@Param("orderId") Long orderId);
	
}
