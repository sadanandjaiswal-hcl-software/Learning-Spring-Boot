package com.learning.mongoDBSpringBoot;

import com.learning.mongoDBSpringBoot.entity.Address;
import com.learning.mongoDBSpringBoot.entity.Order;
import com.learning.mongoDBSpringBoot.entity.Product;
import com.learning.mongoDBSpringBoot.repository.OrderRepository;
import com.learning.mongoDBSpringBoot.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RelationshipTests {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testCreateProduct(){
        Product product = createProductHelper("Earphone", 999.9, 23, 4.2);
        System.out.println(product);

    }

    private Product createProductHelper(
            String name, double price, int stock, double reviews
    ){
        Product product = Product.builder()
                .stock(stock)
                .tags(List.of("Electronic", "Phone", "Gaming", "Editing"))
                .name(name)
                .price(price)
                .category("Electronics")
                .reviews(reviews)
                .build();

        productRepository.insert(product);

        return product;
    }

    @Test
    public void testOrderCreation(){
        List<Product> products = new ArrayList<>();

        Product product1 = createProductHelper("Football", 455, 2, 4.2);
        products.add(product1);
        System.out.println(product1);

        Product product2 = createProductHelper("Bat", 999, 1, 4.8);
        products.add(product2);
        System.out.println(product2);

        Product product3 = createProductHelper("Shoes", 1999, 3, 4.5);
        products.add(product3);
        System.out.println(product3);

        Order order = Order.builder()
                .status("PENDING")
                .quantity(2)
                .totalPrice(120.0)
                .address(Address.builder()
                        .city("Thane")
                        .country("India")
                        .state("Maharashtra")
                        .zipCode("401105")
                        .build()
                )
                .products(products)
                .build();

        orderRepository.insert(order);
        System.out.println(order);
    }

    @Test
    public void testFindByCityName(){
        // Query on Embedding
//        find using query: { "address.city" : "Thane"}
        List<Order> orders = orderRepository.findByAddressCity("Thane");
        for(Order order: orders){
            System.out.println(order);
        }
    }
}
