package com.isep.certification.orders.models.dtos;

import java.math.BigDecimal;


import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String label;
    private Integer quantity;
    private BigDecimal price;
    private String orderDate;
}
