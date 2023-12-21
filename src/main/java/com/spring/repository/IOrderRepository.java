package com.spring.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.dto.AddItem;
import com.spring.dto.QueryResponse;
import com.spring.model.Order;
import com.spring.model.OrderItem;
import com.spring.model.Refund;


@Repository
public interface IOrderRepository  extends PagingAndSortingRepository<Order,Integer>{

	//List<Order> getAllOrders(int userId);
@Query("select a from  Order a  where a.userId=:userId and a.isActive=1 ORDER BY a.createdDate DESC")
List<Order> getAllOrders(@Param("userId")int userId);
//@Modifying
//@Query("UPDATE Order SET status=:status and paymentId=:paymentId WHERE id=:orderId") 
//void updateStatus(@Param("orderId")int orderId,@Param("paymentId") String paymentId,@Param("status") String status);
//@Query("select o from Order o where o.status=:status and (o.createdDate BETWEEN DATE_SUB(NOW(), INTERVAL 2 DAY) AND NOW())")
//List<Order> getOrders(@Param("status")String status);
@Modifying
@Query("UPDATE Order SET isActive=0  WHERE id=:orderId") 
void updatOrderRefund(@Param("orderId")int orderId);
@Query("select r from Refund r where  r.userId=:userId ORDER BY r.requestDate DESC")
List<Refund> getAllRefunds(int userId);
@Query(value="SELECT distinct o.*\r\n"
		+ "FROM lms_payment.orders  o\r\n"
		+ "join lms_payment.orderitems oi on oi.orderId=o.id\r\n"
		+ "WHERE \r\n"
		+ " ((:period = 'year' AND YEAR(o.createdDate) = YEAR(CURRENT_DATE) ) OR\r\n"
		+ "  ( :period = 'month' AND YEAR(o.createdDate) = YEAR(CURRENT_DATE) AND MONTH(createdDate) = MONTH(CURRENT_DATE) ) OR\r\n"
		+ "  ( :period = 'week' AND Year(o.createdDate) = YEAR(CURRENT_DATE) AND WEEK(createdDate) = WEEK(CURRENT_DATE) ) OR\r\n"
		+ "  ( :period = 'day' AND o.createdDate = CURRENT_DATE ) OR\r\n"
		+ "(((:period='3') or(:period='6')) AND  o.createdDate >= DATE_SUB(CURRENT_DATE, INTERVAL '6' MONTH))) and\r\n"
		+ "(COALESCE(:users) IS NULL OR o.userId IN (:users))\r\n"
		+ " ORDER BY EXTRACT(MONTH FROM o.createdDate)",nativeQuery=true)
List<Order> findByBranchAndMonth(@Param("period")String period,@Param("users") Set<Integer> users);

@Query("SELECT   oi.productId ,ROUND(SUM(oi.price)) as percentage \r\n"
		+ "FROM OrderItem  oi join Order o on o.id=oi.orderId\r\n"
		+ "where oi.productId in (:list) and (COALESCE(:users) IS NULL OR o.userId IN (:users))\r\n"
		+ "group by oi.productId")
List<Object[]> getPurchasedCount(@Param("list")List<Integer> list, @Param("users") Set<Integer> users);
	
}
