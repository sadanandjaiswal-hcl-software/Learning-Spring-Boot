package com.learning.mongoDBSpringBoot.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "products")
@Builder
public class Product {
    @Id
    private String id;

    private String name;

    @Indexed
    private String category;

    private double price;

    private List<String> tags;
    private int stock;
    private double reviews;
}
