package com.learning.mongoDBSpringBoot.repository;

import com.learning.mongoDBSpringBoot.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByStatus(String status);

    List<Order> findByTotalPriceGreaterThan(double price);

    List<Order> findByStatusAndQuantityLessThan(String status, int quantity);

    Optional<Order> findFirstByStatusOrderByTotalPriceDesc(String status);
//    Optional<Order> findByStatusOrderByTotalPriceDesc(String status);

    @Query("{status: ?0, totalPrice: {$gte: ?1}}")
    List<Order> findOrdersByStatusAndPriceAbove(String status, double price);
}
