package se.lanteam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import se.lanteam.domain.OrderComment;

@Repository
public interface OrderCommentRepository extends JpaRepository<OrderComment, Long> {

	@Query("SELECT o FROM OrderComment o WHERE o.status = :status")
    public List<OrderComment> findOrderCommentsByStatus(@Param("status") String status);

}
