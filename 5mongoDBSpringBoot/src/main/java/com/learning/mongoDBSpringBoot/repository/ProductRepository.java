package com.learning.mongoDBSpringBoot.repository;

import com.learning.mongoDBSpringBoot.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
