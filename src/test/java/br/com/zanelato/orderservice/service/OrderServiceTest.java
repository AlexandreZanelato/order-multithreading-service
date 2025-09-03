package br.com.zanelato.orderservice.service;

import br.com.zanelato.orderservice.dto.OrderRequest;
import br.com.zanelato.orderservice.dto.OrderResponse;
import br.com.zanelato.orderservice.model.Order;
import br.com.zanelato.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void givenAValidOrder_whenCallProcessOrder_thenShouldProcessOrder() throws ExecutionException, InterruptedException {
        final var expectedProduct = "Notebook";
        final var expectedQuantity = 2;
        final var aOrder = new OrderRequest(expectedProduct, expectedQuantity);

        CompletableFuture<OrderResponse> future = orderService.processOrder(aOrder);

        OrderResponse orderResponse = future.get();

        assertNotNull(orderResponse.message());
        assertEquals("Pedido processado!", orderResponse.message());

        Mockito.verify(orderRepository, Mockito.times(2))
                .save(Mockito.any(Order.class));

    }
}