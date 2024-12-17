package com.isep.certification.orders.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isep.certification.orders.models.entities.Order;
import com.isep.certification.users.models.entities.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByUserOrderByIdDesc(User user);
}
