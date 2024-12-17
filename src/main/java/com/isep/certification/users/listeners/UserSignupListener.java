package com.isep.certification.users.listeners;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import com.isep.certification.commons.utils.Tools;
import com.isep.certification.orders.models.entities.Order;
import com.isep.certification.orders.repositories.OrderRepository;
import com.isep.certification.orders.services.OrderService;
import com.isep.certification.users.models.dtos.UserSignupEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSignupListener {

        private final OrderRepository orderRepository;

    @Async
    @TransactionalEventListener(classes = UserSignupEvent.class, phase = TransactionPhase.AFTER_COMPLETION)
    public void onSignupAddOrder(UserSignupEvent event) {


         List<String> products = Arrays.asList(
            "Dell Inspiron 15", 
            "iPhone 15 Pro Max", 
            "Samsung Galaxy Z Fold 6", 
            "Apple Watch Ultra", 
            "Macbook Pro 16 M4", 
            "Asus ROG", 
            "Playstation 5", 
            "Xbox Series X", 
            "Nintendo Switch", 
            "Canon EOS R5"
        );

        Random random = new Random();
        int randomIndex = random.nextInt(products.size());

        int number = random.nextInt(9999999);

        Order order = new Order();
        order.setUser(event.getUser());
        order.setLabel(products.get(randomIndex));
        order.setOrderDate(Tools.getTodayDateInString());
        order.setPrice(new BigDecimal(String.format("%06d", number)));
        order.setQuantity(1);
       
        orderRepository.save(order);

    }
}
