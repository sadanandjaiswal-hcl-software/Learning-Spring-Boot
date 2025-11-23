package com.learning.mongoDBSpringBoot.controller;

import com.learning.mongoDBSpringBoot.entity.Order;
import com.learning.mongoDBSpringBoot.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderDetails){
        Order order = Order.builder()
                .status((String) orderDetails.get("status"))
                .quantity((Integer) orderDetails.get("quantity"))
                .totalPrice((Double) orderDetails.get("totalPrice"))
                .build();

        System.out.println("Order to save: " + order);
//        orderRepository.save(order);


//        orderRepository.insert(order);
        orderRepository.insert(order);

        return ResponseEntity.ok(order);
    }
}
