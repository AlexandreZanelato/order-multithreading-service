package br.com.zanelato.orderservice.controller;

import br.com.zanelato.orderservice.dto.OrderRequest;
import br.com.zanelato.orderservice.dto.OrderResponse;
import br.com.zanelato.orderservice.model.Status;
import br.com.zanelato.orderservice.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = Objects.requireNonNull(orderService);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.processOrder(orderRequest);
    }

    @GetMapping("/{orderId}/status")
    public Status getOrderStatus(@PathVariable("orderId") String orderId) {
        return orderService.getOrderStatus(orderId);
    }

}
