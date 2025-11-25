package com.learning.mongoDBSpringBoot;

import com.learning.mongoDBSpringBoot.entity.Order;
import com.learning.mongoDBSpringBoot.entity.Product;
import com.learning.mongoDBSpringBoot.entity.dto.OrderSearchCriteria;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
import lombok.Data;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class MongoTemplateTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testMongoTemplate(){
        /*
        List<Order> orders = mongoTemplate.findAll(Order.class);
        orders.forEach(System.out::println);
         */

        // is, in, and, or, alike, like, gt, lt, gte, lte

        /*
        Query query = new Query(
                Criteria.where("status").is("PENDING")
        );
        List<Order> orders = mongoTemplate.find(query, Order.class);
         */

        /*
        Query query = new Query(
                Criteria.where("status").in("PENDING", "CANCELLED")
                        .and("totalPrice").gte(100)
        );
        List<Order> orders = mongoTemplate.find(query, Order.class);
         */

        /*
        Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("status").is("CONFIRMED"),
                        Criteria.where("totalPrice").lte(500)
                ).andOperator(
                        Criteria.where("status").is("CONFIRMED"),
                        Criteria.where("quantity").gte(8)
                )
        );
         */

        Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("status").is("CONFIRMED"),
                        Criteria.where("totalPrice").lte(500)
                ).andOperator(
                        Criteria.where("status").is("CONFIRMED"),
                        Criteria.where("quantity").gte(8)
                )
        );

        query.fields().include("status", "totalPrice", "quantity", "_id");  // Rest fields will be there as We are getting response in Order Dto but they will be null
        query.limit(3);

        List<Order> orders = mongoTemplate.find(query, Order.class);

        orders.forEach(System.out::println);
    }

    @Test
    public void testDynamicFilterWithMongoTemplate(){
        OrderSearchCriteria orderSearchCriteria1 = OrderSearchCriteria.builder()
                .status("CONFIRMED")
                .quantity(8)
                .build();

        OrderSearchCriteria orderSearchCriteria2 = OrderSearchCriteria.builder()
                .totalPrice(1250.0)
                .build();

        List<Order> orders1 = filterDynamicQuery(orderSearchCriteria1);
        orders1.forEach(System.out::println);

        System.out.println("*************************");

        List<Order> orders2 = filterDynamicQuery(orderSearchCriteria2);
        orders2.forEach(System.out::println);

    }

    // Dynamic Filter
    public List<Order> filterDynamicQuery(OrderSearchCriteria searchCriteria){
        Query query = new Query();

        if(searchCriteria.getStatus()!=null){
            query.addCriteria(
                    Criteria.where("status").is(searchCriteria.getStatus())
            );
        }

        if(searchCriteria.getQuantity()!=null){
            query.addCriteria(
                    Criteria.where("quantity").gte(searchCriteria.getQuantity())
            );
        }

        if(searchCriteria.getTotalPrice()!=null){
            query.addCriteria(
                    Criteria.where("totalPrice").lte(searchCriteria.getTotalPrice())
            );

//            query.with(Sort.by(Sort.Direction.DESC, "totalPrice"));
        }

        if(searchCriteria.getSortBy()!=null){
            Sort.Direction direction = searchCriteria.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;

            query.with(Sort.by(direction, searchCriteria.getSortBy()));
        }

        if(searchCriteria.getPage()!=null){
            Pageable pageable = PageRequest.of(
                    searchCriteria.getPage(),
                    searchCriteria.getSize()
            );

            query.with(pageable);
        }

        return mongoTemplate.find(query, Order.class);
    }

    @Test
    public void testQueryWithDBRef(){
        // find Product
        Product product = mongoTemplate.findOne(
                Query.query(Criteria.where("name").is("Earphone")),
                Product.class
        );

        System.out.println("product : " + product);

        Query query = new Query(
                Criteria.where("products.$id").is(new ObjectId(product.getId()))
        );

        List<Order> orders = mongoTemplate.find(query, Order.class);

        orders.forEach(System.out::println);
    }

    @Test
    public void testMongoTemplateMultiUpdate(){
        List<String> statusList = Arrays.asList("CONFIRMED", "PENDING");

        Map<String, String> statusMap = Map.of(
                "SHIPPED", "DELIVERED",
                "PENDING", "SHIPPED",
                "CONFIRMED", "PENDING"
        );

        for (String status: statusList){
            Query query = new Query(
                    Criteria.where("status").is(status)
            );

            Update update = new Update()
                    .set("status", statusMap.get(status))
                    .set("updatedAt", new Date());

            mongoTemplate.updateMulti(query, update, Order.class);

        }
    }

    @Test
    public void upsertSingleProduct() {
        Query query = new Query(
                Criteria.where("name").is("Basketball")
        );

        Update update = new Update()
                .set("category", "Sports")
                .set("price", 29.99)
                .set("stock", 50)
                .addToSet("tags").each("Sports", "Outdoor")
                .setOnInsert("reviews", 4.0)              // Only on first insert
                .setOnInsert("createdAt", LocalDateTime.now());

        UpdateResult result = mongoTemplate.upsert(query, update, Product.class);

        if(result.getUpsertedId() != null){
            System.out.println("Created new Order!!");
        }else{
            System.out.println("Updated Existing Order.");
        }
    }

}

