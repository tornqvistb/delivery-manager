package se.lanteam.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.OrderHeader;

@Repository
public interface OrderRepository extends JpaRepository<OrderHeader, Long> {

	static final String LIKE_CONDITION_ORDER_HEADER = 
			 "(LOWER(o.orderNumber) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.customerOrderNumber) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.customerSalesOrder) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.netsetOrderNumber) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.customerNumber) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.customerName) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.postalAddress1) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.postalAddress2) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.postalCode) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.city) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.deliveryAddressName) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.deliveryPostalAddress1) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.deliveryPostalAddress2) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.deliveryPostalCode) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.deliveryCity) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.leasingNumber) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.contact1Name) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.contact1Email) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.contact1Phone) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.contact2Name) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.contact2Email) LIKE LOWER(:searchString) OR "
			+ "LOWER(o.contact2Phone) LIKE LOWER(:searchString) OR "						
			+ "LOWER(o.articleNumbers) LIKE LOWER(:searchString))";

	
	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList")
    public List<OrderHeader> findOrdersByStatusList(@Param("statusList") List<String> statusList);
	
	@Query("SELECT o FROM OrderHeader o WHERE o.status = :status")
    public List<OrderHeader> findOrdersByStatus(@Param("status") String status);

	@Query("SELECT o FROM OrderHeader o WHERE o.orderNumber = :orderNumber")
    public List<OrderHeader> findOrdersByOrderNumber(@Param("orderNumber") String orderNumber);

	@Query("SELECT o FROM OrderHeader o WHERE o.customerSalesOrder LIKE %:customerSalesOrder%")
    public List<OrderHeader> findOrdersByCustomerSalesOrder(@Param("customerSalesOrder") String customerSalesOrder);

	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList AND o.deliveryDate >= :deliveryStartDate AND o.deliveryDate <= :deliveryEndDate AND "
			+ LIKE_CONDITION_ORDER_HEADER
			+ " AND o.customerGroup.id = :customerGroupId")
    public List<OrderHeader> findDeliveredOrdersFromSearch(@Param("statusList") List<String> statusList, @Param("searchString") String searchString, @Param("deliveryStartDate") Date deliveryStartDate, @Param("deliveryEndDate") Date deliveryEndDate, @Param("customerGroupId") Long customerGroupId, Pageable pageable);

	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList AND "
			+ LIKE_CONDITION_ORDER_HEADER
			+ " AND o.customerGroup.id = :customerGroupId AND excludeFromList in :excludeList")
    public List<OrderHeader> findOrdersFromSearch(@Param("statusList") List<String> statusList, @Param("searchString") String searchString, @Param("customerGroupId") Long customerGroupId, @Param("excludeList") List<Boolean> excludeList, Pageable pageable);

	// SLA queries
	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList AND o.orderDate > :orderDate AND o.deliveryDate >= :deliveryStartDate AND o.deliveryDate <= :deliveryEndDate AND o.customerGroup.id = :customerGroupId")
    public List<OrderHeader> findDeliveredOrdersFromSearchSLAByCustGroup(@Param("statusList") List<String> statusList, @Param("orderDate") Date orderDate, @Param("deliveryStartDate") Date deliveryStartDate, @Param("deliveryEndDate") Date deliveryEndDate, @Param("customerGroupId") Long customerGroupId);

	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList AND o.orderDate > :orderDate AND o.deliveryDate >= :deliveryStartDate AND o.deliveryDate <= :deliveryEndDate")
    public List<OrderHeader> findDeliveredOrdersFromSearchSLA(@Param("statusList") List<String> statusList, @Param("orderDate") Date orderDate, @Param("deliveryStartDate") Date deliveryStartDate, @Param("deliveryEndDate") Date deliveryEndDate);
	
	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList")
    public List<OrderHeader> findOrdersFromSearchSLA(@Param("statusList") List<String> statusList);

	@Query("SELECT o FROM OrderHeader o WHERE o.status in :statusList AND o.customerGroup.id = :customerGroupId")
    public List<OrderHeader> findOrdersFromSearchSLAByCustGroup(@Param("statusList") List<String> statusList, @Param("customerGroupId") Long customerGroupId);
	
	@Transactional
	@Modifying	
	@Query("UPDATE OrderHeader o SET o.toBeArchived = :toBeArchived WHERE o.deliveryDate < :lastDeliveryDate AND o.status = :status")
    public void setArchiving(@Param("toBeArchived") boolean archived, @Param("lastDeliveryDate") Date lastDeliveryDate, @Param("status") String status);

	@Query("SELECT COUNT(*) FROM OrderHeader o WHERE o.deliveryDate < :lastDeliveryDate AND o.status = :status")
    public Integer countOrdersForArchiving(@Param("lastDeliveryDate") Date lastDeliveryDate, @Param("status") String status);

	@Query("SELECT COUNT(*) FROM OrderHeader o WHERE o.status in :statusList")
    public Integer countOrdersByStatusList(@Param("statusList") List<String> statusList);

	@Query("SELECT MIN(o.deliveryDate) FROM OrderHeader o WHERE o.status = :status")
	public Date getFirstDeliveryDate(@Param("status") String status);

	@Query("SELECT o FROM OrderHeader o WHERE o.deliveryPlan.plannedDeliveryDate = :planDate and o.customerGroup.id = :customerGroupId")
    public List<OrderHeader> findOrdersByPlanDate(@Param("planDate") Date planDate, @Param("customerGroupId") Long customerGroupId);

	// Delivery report queries
	@Query("SELECT o FROM OrderHeader o WHERE o.deliveryDate >= :firstDate AND o.deliveryDate <= :lastDate AND o.orderNumber >= :firstOrderNo AND o.orderNumber <= :lastOrderNo")
    public List<OrderHeader> findDeliveredOrders(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("firstOrderNo") String firstOrderNo, @Param("lastOrderNo") String lastOrderNo);

	@Query("SELECT o FROM OrderHeader o WHERE o.deliveryDate >= :firstDate AND o.deliveryDate <= :lastDate AND o.orderNumber >= :firstOrderNo AND o.orderNumber <= :lastOrderNo AND o.customerGroup.id = :customerGroupId")
    public List<OrderHeader> findDeliveredOrdersByCustGroup(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("firstOrderNo") String firstOrderNo, @Param("lastOrderNo") String lastOrderNo, @Param("customerGroupId") Long customerGroupId);

	@Query("SELECT o FROM OrderHeader o WHERE o.netsetOrderNumber = :netsetOrderNumber")
    public List<OrderHeader> findOrdersByNetsetOrderNumber(@Param("netsetOrderNumber") String netsetOrderNumber);
	
	@Query("SELECT o FROM OrderHeader o WHERE o.jointDelivery = :jointDelivery")
    public List<OrderHeader> findOrdersByJointDelivery(@Param("jointDelivery") String jointDelivery);

	@Query("SELECT o FROM OrderHeader o WHERE status = :status AND o.jointDelivery != NULL AND o.jointDeliveryText IS NULL")
    public List<OrderHeader> findOrdersJointDeliveryUnjoined(@Param("status") String status);

	@Query("SELECT o FROM OrderHeader o WHERE o.status = :status AND o.creationDate < :theDate")
    public List<OrderHeader> findOrdersByCreationDateAndStatus(@Param("status") String status, @Param("theDate") Date theDate);

	@Query("SELECT o FROM OrderHeader o WHERE o.customerGroup.id = :customerGroupId")
    public List<OrderHeader> findOrdersByCustGroup(@Param("customerGroupId") Long customerGroupId);

	@Query("SELECT o FROM OrderHeader o WHERE o.jointDelivery = :jointDelivery AND o.status = :status")
    public List<OrderHeader> findOrdersByJointDeliveryAndStatus(@Param("jointDelivery") String jointDelivery, @Param("status") String status);

	@Query("SELECT o FROM OrderHeader o WHERE o.netsetOrderNumber = :netsetOrderNumber AND o.status = :status")
    public List<OrderHeader> findOrdersByNetsetOrderNumberAndStatus(@Param("netsetOrderNumber") String netsetOrderNumber, @Param("status") String status);

	@Query("SELECT o FROM OrderHeader o WHERE o.customerOrderNumber LIKE %:customerOrderNumber%")
    public List<OrderHeader> findOrdersByCustomerOrderNumber(@Param("customerOrderNumber") String customerOrderNumber);

}
