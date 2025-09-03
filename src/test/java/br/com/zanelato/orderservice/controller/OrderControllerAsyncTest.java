package br.com.zanelato.orderservice.controller;

import br.com.zanelato.orderservice.dto.OrderRequest;
import br.com.zanelato.orderservice.dto.OrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrderControllerAsyncTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldHandleMultipleAsyncRequestsInParallel() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        int totalRequests = 10;

        List<CompletableFuture<ResponseEntity<OrderResponse>>> futures = IntStream
                .range(0, totalRequests)
                .mapToObj(i -> {
                    OrderRequest request = new OrderRequest(STR."Notebook \{i}", i);
                    return CompletableFuture.supplyAsync(() ->
                            restTemplate.postForEntity("/orders", request, OrderResponse.class)
                    );
                })
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long end = System.currentTimeMillis();
        System.out.println(STR."Tempo total: \{end - start}ms");

        for (CompletableFuture<ResponseEntity<OrderResponse>> future : futures) {
            ResponseEntity<OrderResponse> response = future.get();
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals("Pedido processado!", response.getBody().message());
        }

    }
}
