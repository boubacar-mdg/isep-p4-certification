package com.isep.certification.orders.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isep.certification.orders.models.dtos.OrderResponse;
import com.isep.certification.orders.models.entities.Order;
import com.isep.certification.orders.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.isep.certification.commons.utils.Constants.BASE_MAPPING;

@RestController
@RequiredArgsConstructor 
@RequestMapping(BASE_MAPPING+"/orders")
@Slf4j
@Tag(name = "Commandes", description = "Gestions des commandes")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Point de terminaison permettant de récupérer la liste des commmandes du client authentifié")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    })
    public ResponseEntity<List<OrderResponse>> getOrdersForAccount() {
        return ResponseEntity.ok(orderService.listAllOrders());
    }

}
