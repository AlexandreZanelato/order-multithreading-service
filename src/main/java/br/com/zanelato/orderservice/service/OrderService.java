package br.com.zanelato.orderservice.service;

import br.com.zanelato.orderservice.dto.OrderRequest;
import br.com.zanelato.orderservice.dto.OrderResponse;
import br.com.zanelato.orderservice.model.Order;
import br.com.zanelato.orderservice.model.Status;
import br.com.zanelato.orderservice.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }

    @Async("taskExecutor")
    public CompletableFuture<OrderResponse> processOrder(final OrderRequest orderRequest) {
        String orderId = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        Order order = new Order(
                orderId,
                orderRequest.quantity(),
                Status.PROCESSING,
                createdAt
        );

        orderRepository.save(order);

        logger.info("Pedido [{}] iniciado na thread [{}] com status [{}]", orderId, Thread.currentThread().getName(), order.getStatus());

        return CompletableFuture.supplyAsync(() -> {
            order.setStatus(Status.COMPLETED);
            orderRepository.save(order);
            logger.info("Pedido [{}] finalizado na thread [{}] com status [{}]", orderId, Thread.currentThread().getName(), order.getStatus());
            return new OrderResponse(orderId, "Pedido processado!");
        }, CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS));

    }

    public Status getOrderStatus(String orderId) {
        return orderRepository.findById(orderId)
                .map(Order::getStatus)
                .orElse(Status.NOT_FOUND);
    }

}
