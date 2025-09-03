package br.com.zanelato.orderservice.service;

import br.com.zanelato.orderservice.dto.OrderRequest;
import br.com.zanelato.orderservice.dto.OrderResponse;
import br.com.zanelato.orderservice.model.Order;
import br.com.zanelato.orderservice.model.Status;
import br.com.zanelato.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void givenAValidOrder_whenCallProcessOrder_thenShouldProcessOrderAndSaveToRepository() throws ExecutionException, InterruptedException {
        final var expectedProduct = "Notebook";
        final var expectedQuantity = 2;
        final var aOrder = new OrderRequest(expectedProduct, expectedQuantity);

        Assertions.assertEquals(0, orderRepository.count());

        CompletableFuture<OrderResponse> future = orderService.processOrder(aOrder);

        OrderResponse orderResponse = future.get();

        Assertions.assertEquals(1, orderRepository.count());

        Order actualOrder = orderRepository.findById(orderResponse.orderId()).orElseThrow();

        Assertions.assertNotNull(actualOrder.getOrderId());
        Assertions.assertEquals(Status.COMPLETED, actualOrder.getStatus());
        Assertions.assertEquals(2, actualOrder.getQuantity());
        Assertions.assertNotNull(actualOrder.getCreatedAt());

    }

}
