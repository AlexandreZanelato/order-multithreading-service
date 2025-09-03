package br.com.zanelato.orderservice.config;

import br.com.zanelato.orderservice.dto.OrderRequest;
import br.com.zanelato.orderservice.dto.OrderResponse;
import br.com.zanelato.orderservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@ActiveProfiles("test")
class AsyncConfigTest {

    @Autowired
    private OrderService orderService;

    @Test
    void shouldRunAsyncMethodInDifferentThread() throws ExecutionException, InterruptedException {
        final var expectedProduct = "Notebook";
        final var expectedQuantity = 2;
        final var aOrder = new OrderRequest(expectedProduct, expectedQuantity);

        CompletableFuture<OrderResponse> future = orderService.processOrder(aOrder);

        Assertions.assertFalse(future.isDone());
        OrderResponse response = future.get();

        Assertions.assertEquals("Pedido processado!", response.message());

    }

}
