package com.isep.certification.orders.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.isep.certification.orders.models.dtos.OrderResponse;
import com.isep.certification.orders.models.entities.Order;
import com.isep.certification.orders.repositories.OrderRepository;
import com.isep.certification.users.models.entities.User;
import com.isep.certification.users.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public List<OrderResponse> listAllOrders() {
        User user = userService.findUserByPhoneNumber(getAuthenticatedUser());
        return orderRepository.findByUserOrderByIdDesc(user).stream()
                .map((order) -> modelMapper.map(order, OrderResponse.class)).toList();
    }

    @Override
    public Order findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
