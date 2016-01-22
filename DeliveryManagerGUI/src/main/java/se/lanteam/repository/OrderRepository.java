package se.lanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.OrderHeader;

@Repository
public interface OrderRepository extends JpaRepository<OrderHeader, Long> {

}
