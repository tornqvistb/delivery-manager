package se.lanteam.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.OrderHeader;

@Repository
public interface OrderRepository extends JpaRepository<OrderHeader, Long> {

	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList")
    public List<OrderHeader> findOrdersByStatusList(@Param("statusList") List<String> statusList);
	
	@Query("SELECT o FROM OrderHeader o WHERE o.status = :status")
    public List<OrderHeader> findOrdersByStatus(@Param("status") String status);

	@Query("SELECT o FROM OrderHeader o WHERE o.orderNumber = :orderNumber")
    public List<OrderHeader> findOrdersByOrderNumber(@Param("orderNumber") String orderNumber);

	@Query("SELECT o FROM OrderHeader o WHERE o.customerSalesOrder = :customerSalesOrder")
    public List<OrderHeader> findOrdersByCustomerSalesOrder(@Param("customerSalesOrder") String customerSalesOrder);

	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList AND o.orderDate > :orderDate")
    public List<OrderHeader> findOrdersByStatusListAfterDate(@Param("statusList") List<String> statusList, @Param("orderDate") Date orderDate);
	
	@Query("SELECT o FROM OrderHeader o WHERE o.status = :status AND o.orderDate > :orderDate")
    public List<OrderHeader> findOrdersByStatusAfterDate(@Param("status") String status, @Param("orderDate") Date orderDate);

	
}
