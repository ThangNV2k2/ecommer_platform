package com.doan.backend.repositories;

import com.doan.backend.dto.response.CategoryStatisticResponse;
import com.doan.backend.dto.response.CustomerStatistic.CustomerStatisticResponse;
import com.doan.backend.dto.response.ProductRevenueResponse;
import com.doan.backend.dto.response.ProductStatisticResponse;
import com.doan.backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    Iterable<OrderItem> findByOrderId(String orderId);

    // Doanh thu từng sản phẩm theo ngày
    @Procedure(name = "GetRevenueByDay")
    List<ProductRevenueResponse> getRevenueByDate(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


    // Doanh thu từng sản phẩm theo tuần
//    @Query("SELECT p.name AS productName,SUM(oi.quantity) AS totalQuantity, SUM(oi.quantity * oi.price) AS revenue " +
//            "FROM OrderItem oi " +
//            "JOIN Product p ON oi.product_id = p.id " +
//            "JOIN Order o ON oi.orderId = o.id " +
//            "WHERE FUNCTION('WEEK', o.updatedAt) = FUNCTION('WEEK', :date) AND FUNCTION('YEAR', o.updatedAt) = FUNCTION('YEAR', :date) " +
//            "AND o.status = 5 " +
//            "GROUP BY p.name " +
//            "ORDER BY SUM(oi.quantity * oi.price) DESC")
//    List<ProductRevenueResponse> findRevenueByProductAndWeek(@Param("date") LocalDate date);

    // Doanh thu từng sản phẩm theo tháng
    @Procedure(name = "GetRevenueByMonth")
    List<ProductRevenueResponse> getRevenueByMonth(@Param("month") int month, @Param("year") int year);


    // Doanh thu từng sản phẩm theo quý
    @Procedure(name = "GetRevenueByQuarter")
    List<ProductRevenueResponse> GetRevenueProductByQuarter(@Param("quarter") int quarter, @Param("year") int year);


    // Doanh thu từng sản phẩm theo năm
    @Procedure(name = "GetRevenueByYear")
    List<ProductRevenueResponse> GetRevenueProductByYear(@Param("year") int year);

    @Query("""
        SELECT new com.doan.backend.dto.response.ProductStatisticResponse(
            p.name,
            o.updatedAt,
            s.name,
            oi.price,
            oi.quantity,
            COALESCE(pr.discountPercentage, 0)
        )
        FROM OrderItem oi
        JOIN oi.order o
        JOIN oi.product p
        JOIN ProductInventory pi ON p.id = pi.product.id
        JOIN pi.size s
        LEFT JOIN oi.promotion pr
        WHERE o.updatedAt BETWEEN :startDate AND :endDate
    """)
    List<ProductStatisticResponse> getProductRevenue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
        SELECT new com.doan.backend.dto.response.CategoryStatisticResponse(
            c.name,
            p.name,
            o.updatedAt,
            s.name,
            oi.price,
            oi.quantity,
            COALESCE(pr.discountPercentage, 0)
        )
        FROM OrderItem oi
        JOIN oi.order o
        JOIN oi.product p
        JOIN p.category c ON p.category.id = c.id
        JOIN ProductInventory pi ON p.id = pi.product.id
        JOIN pi.size s
        LEFT JOIN oi.promotion pr
        WHERE o.updatedAt BETWEEN :startDate AND :endDate
    """)
    List<CategoryStatisticResponse> getCategoryRevenue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
    SELECT new com.doan.backend.dto.response.CustomerStatistic.CustomerStatisticResponse(
        u.id,
        u.name,
        u.email,
        o.id,
        o.totalPriceAfterDiscount,
        i.updatedAt)
    FROM Invoice i
    INNER JOIN i.order o
    INNER JOIN o.user u
    INNER JOIN u.roles r
    WHERE i.status = 'PAID' AND r = 'CUSTOMER'
          AND (o.updatedAt BETWEEN :startDate AND :endDate)
    """)
    List<CustomerStatisticResponse> getCustomerRevenue(LocalDateTime startDate, LocalDateTime endDate);
}