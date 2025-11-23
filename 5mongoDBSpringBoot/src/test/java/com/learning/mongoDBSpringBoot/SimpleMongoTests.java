package com.learning.mongoDBSpringBoot;

import com.learning.mongoDBSpringBoot.entity.Order;
import com.learning.mongoDBSpringBoot.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
public class SimpleMongoTests {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testCreateOrder() {
        Order order = Order.builder()
                .status("PENDING")
                .quantity(1)
                .totalPrice(105.5)
                .build();

        orderRepository.insert(order);

        System.out.println("order detail : " + order);
    }

    @Test
    void testCreateMultipleOrders() {

        Order o1 = Order.builder()
                .status("OUT FOR DELIVERY")
                .quantity(2)
                .totalPrice(400.0)
                .build();

        Order o2 = Order.builder()
                .status("OUT FOR DELIVERY")
                .quantity(1)
                .totalPrice(120.0)
                .build();

        Order o3 = Order.builder()
                .status("CANCELED")
                .quantity(3)
                .totalPrice(170.0)
                .build();

        Order o4 = Order.builder()
                .status("DELIVERED")
                .quantity(4)
                .totalPrice(248.2)
                .build();

        List<Order> savedOrders = orderRepository.insert(List.of(o1, o2, o3, o4));

        savedOrders.forEach(System.out::println);
    }


    @Test
    public void testGetOrders(){
        List<Order> orders = orderRepository.findAll();
        for(Order order : orders){
            System.out.println(order);
        }
    }

    @Test
    public void testFindByStatus() {
        List<Order> orders = orderRepository.findByStatus("DELIVERED");

        System.out.println("Orders with status = DELIVERED:");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    @Test
    public void testFindByTotalPriceGreaterThan() {
        List<Order> expensiveOrders = orderRepository.findByTotalPriceGreaterThan(350.0);

        System.out.println("Orders with price > 350:");
        for (Order order : expensiveOrders) {
            System.out.println(order);
        }
    }

    @Test
    public void testFindByStatusAndQuantityLessThan() {
        Integer quantity = 5;
        System.out.println("quantity: " + quantity);
        List<Order> orders = orderRepository.findByStatusAndQuantityLessThan("DELIVERED", quantity);

        System.out.println("Orders with status=READY and quantity < "+quantity);
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    @Test
    public void testFindFirstByStatusOrderByTotalPrice() {
        String status = "OUT FOR DELIVERY";
        Optional<Order> orders1 = orderRepository.findFirstByStatusOrderByTotalPriceDesc(status);
//        Optional<Order> orders = orderRepository.findByStatusOrderByTotalPriceDesc(status);

        System.out.println("order1: " + orders1);
//        System.out.println("All Orders");
//        System.out.println(orders);
    }

    @Test
    public void testFindOrdersByStatusAndPriceAbove(){
        String status = "OUT FOR DELIVERY";
        double price = 100.0;
        List<Order> orders = orderRepository.findOrdersByStatusAndPriceAbove(status, price);

        for (Order order : orders) {
            System.out.println(order);
        }
    }

    @Test
    public void testUpdateOrder() {

        // 1. Find the existing order
        Order order = orderRepository.findById("6922ffb2df3d72b7d0444994").orElseThrow(); // get first order for test
        System.out.println("Before update: " + order);

        // 2. Update fields
        order.setStatus("DELIVERED");
        order.setTotalPrice(400.);

        // 3. Save (automatically updates because ID exists)
        orderRepository.save(order);

        System.out.println("After update: " + order);
    }

    @Test
    public void testDeleteOrder() {

        // 1. Find single order by ID
        Order order = orderRepository.findById("6922c0b0460aefa186679a67")
                .orElseThrow();

        // 3. Delete a single order object
        orderRepository.delete(order);

        // 4. Delete by ID
        orderRepository.deleteById("6922bf6b2b2483377175dc69");

        // 5. Delete multiple IDs using List
        orderRepository.deleteAllById(
                List.of(
                        "6922bf6b2b2483377175dc69",
                        "6922c0b0460aefa186679a67"
                )
        );
    }

    @Test
    public void testDeleteAllOrders() {
        orderRepository.deleteAll();
    }

    @Test
    public void testGetAllOrdersPagination(){
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "totalPrice"));
        Page orderPage = orderRepository.findAll(pageRequest);
        List<Order> ordersList = orderPage.toList();

        System.out.println("orderPage");
        System.out.println(orderPage);

        System.out.println("ordersList");
        for (Order order : ordersList) {
            System.out.println(order);
        }
    }

    @Test
    void testCreateRandomOrders() {

        int numberOfOrders = 10;

        List<String> statuses = List.of("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");

        Random random = new Random();

        List<Order> ordersToCreate = new ArrayList<>();

        for (int i = 0; i < numberOfOrders; i++) {

            String randomStatus = statuses.get(random.nextInt(statuses.size()));
            int randomQty = random.nextInt(10) + 1; // 1 to 10 quantity
            double randomPrice = randomQty * (50 + random.nextInt(150)); // random price 50–200 per unit

            Order order = Order.builder()
                    .status(randomStatus)
                    .quantity(randomQty)
                    .totalPrice(randomPrice)
                    .build();

            ordersToCreate.add(order);
        }

        List<Order> savedOrders = orderRepository.insert(ordersToCreate);

        savedOrders.forEach(System.out::println);
    }


}