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

	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList AND o.orderDate > :orderDate AND o.deliveryDate >= :deliveryStartDate AND o.deliveryDate <= :deliveryEndDate AND (LOWER(o.orderNumber) LIKE LOWER(:searchString) OR LOWER(o.customerOrderNumber) LIKE LOWER(:searchString) OR LOWER(o.customerSalesOrder) LIKE LOWER(:searchString))")
    public List<OrderHeader> findDeliveredOrdersFromSearch(@Param("statusList") List<String> statusList, @Param("orderDate") Date orderDate, @Param("searchString") String searchString, @Param("deliveryStartDate") Date deliveryStartDate, @Param("deliveryEndDate") Date deliveryEndDate);

	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList AND o.orderDate > :orderDate AND (LOWER(o.orderNumber) LIKE LOWER(:searchString) OR LOWER(o.customerOrderNumber) LIKE LOWER(:searchString) OR LOWER(o.customerSalesOrder) LIKE LOWER(:searchString))")
    public List<OrderHeader> findOrdersFromSearch(@Param("statusList") List<String> statusList, @Param("orderDate") Date orderDate, @Param("searchString") String searchString);

}
