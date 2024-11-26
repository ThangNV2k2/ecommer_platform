package com.doan.backend.repositories;

import com.doan.backend.dto.response.ProductRevenueResponse;
import com.doan.backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    Iterable<OrderItem> findByOrderId(String orderId);

    // Doanh thu từng sản phẩm theo ngày
    @Query("SELECT p.name AS productName,SUM(oi.quantity) AS totalQuantity, SUM(oi.quantity * oi.price) AS revenue " +
            "FROM OrderItem oi " +
            "JOIN Product p ON oi.product_id = p.id " +
            "JOIN Order o ON oi.orderId = o.id " +
            "WHERE FUNCTION('DATE', o.updatedAt) = :date " +
            "AND o.status = 5 " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.quantity * oi.price) DESC")
    List<ProductRevenueResponse> findRevenueByProductAndDate(@Param("date") LocalDate date);

    // Doanh thu từng sản phẩm theo tuần
    @Query("SELECT p.name AS productName,SUM(oi.quantity) AS totalQuantity, SUM(oi.quantity * oi.price) AS revenue " +
            "FROM OrderItem oi " +
            "JOIN Product p ON oi.product_id = p.id " +
            "JOIN Order o ON oi.orderId = o.id " +
            "WHERE FUNCTION('WEEK', o.updatedAt) = FUNCTION('WEEK', :date) AND FUNCTION('YEAR', o.updatedAt) = FUNCTION('YEAR', :date) " +
            "AND o.status = 5 " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.quantity * oi.price) DESC")
    List<ProductRevenueResponse> findRevenueByProductAndWeek(@Param("date") LocalDate date);

    // Doanh thu từng sản phẩm theo tháng
    @Query("SELECT p.name AS productName,SUM(oi.quantity) AS totalQuantity, SUM(oi.quantity * oi.price) AS revenue " +
            "FROM OrderItem oi " +
            "JOIN Product p ON oi.product_id = p.id " +
            "JOIN Order o ON oi.orderId = o.id " +
            "WHERE FUNCTION('MONTH', o.updatedAt) = :month AND FUNCTION('YEAR', o.updatedAt) = :year " +
            "AND o.status = 5 " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.quantity * oi.price) DESC")
    List<ProductRevenueResponse> findRevenueByProductAndMonth(@Param("month") int month, @Param("year") int year);


    // Doanh thu từng sản phẩm theo quý
    @Query("SELECT p.name AS productName,SUM(oi.quantity) AS totalQuantity,,SUM(oi.quantity * oi.price) AS revenue " +
            "FROM OrderItem oi " +
            "JOIN Product p ON oi.product_id = p.id " +
            "JOIN Order o ON oi.orderId = o.id " +
            "WHERE FUNCTION('QUARTER', o.updatedAt) = :quarter AND FUNCTION('YEAR', o.updatedAt) = :year " +
            "AND o.status = 5 " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.quantity * oi.price) DESC")
    List<ProductRevenueResponse> findRevenueByProductAndQuarter(@Param("quarter") int quarter, @Param("year") int year);


    // Doanh thu từng sản phẩm theo năm
    @Query("SELECT p.name AS productName,SUM(oi.quantity) AS totalQuantity, SUM(oi.quantity * oi.price) AS revenue " +
            "FROM OrderItem oi " +
            "JOIN Product p ON oi.product_id = p.id " +
            "JOIN Order o ON oi.orderId = o.id " +
            "WHERE FUNCTION('YEAR', o.updatedAt) = :year " +
            "AND o.status = 5 " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.quantity * oi.price) DESC")
    List<ProductRevenueResponse> findRevenueByProductAndYear(@Param("year") int year);

    // Doanh thu từng sản phẩm trong khoảng thời gian chọn
//    @Query("SELECT p.name AS productName,SUM(oi.quantity) AS totalQuantity, SUM(oi.quantity * oi.price) AS revenue " +
//            "FROM OrderItem oi " +
//            "JOIN Product p ON oi.product_id = p.id " +
//            "JOIN Order o ON oi.orderId = o.id " +
//            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
//            "AND o.status = 5 " +
//            "GROUP BY p.name " +
//            "ORDER BY SUM(oi.quantity * oi.price) DESC")
//    List<ProductRevenueResponse> findRevenueByProductBetweenDates(
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate);
}
