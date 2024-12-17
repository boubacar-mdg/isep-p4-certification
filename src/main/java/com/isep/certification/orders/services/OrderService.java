package com.isep.certification.orders.services;

import java.util.List;

import com.isep.certification.orders.models.dtos.OrderResponse;
import com.isep.certification.orders.models.entities.Order;

public interface OrderService {
    List<OrderResponse> listAllOrders();

    Order findById(Long id);
}
