package com.learning.mongoDBSpringBoot.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchCriteria {
    // Filters
    private String status;     // corrected "staus"
    private Double totalPrice;
    private Integer quantity;

    // Sorting
    private String sortBy;
    private boolean ascending = true;

    // Pagination
    private Integer page;
    private Integer size;
}

