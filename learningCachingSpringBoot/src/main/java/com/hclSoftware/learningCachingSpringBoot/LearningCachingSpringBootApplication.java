package com.hclSoftware.learningCachingSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LearningCachingSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningCachingSpringBootApplication.class, args);
	}

}
