package com.learning.mongoDBSpringBoot.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
