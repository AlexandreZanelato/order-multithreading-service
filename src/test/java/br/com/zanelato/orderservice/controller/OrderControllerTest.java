package br.com.zanelato.orderservice.controller;

import br.com.zanelato.orderservice.dto.OrderResponse;
import br.com.zanelato.orderservice.model.Status;
import br.com.zanelato.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.CompletableFuture;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void givenAValidOrder_whenCallProcessOrder_thenShouldProcessOrderAndCreateOrderSuccessfully() throws Exception {
        final var expectedOrderId = "123";
        final var expectedMessage = "Pedido processado!";
        var response = new OrderResponse(expectedOrderId, expectedMessage);

        Mockito.when(orderService.processOrder(Mockito.any()))
                        .thenReturn(CompletableFuture.completedFuture(response));

        final var request = mvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "product": "Notebook",
                                "quantity": 2
                                }
                                """))
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        mvc.perform(MockMvcRequestBuilders.asyncDispatch(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(expectedOrderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedMessage));

    }

    @Test
    void shouldReturnOrderStatus() throws Exception {
        Mockito.when(orderService.getOrderStatus("123"))
                .thenReturn(Status.COMPLETED);
        mvc.perform(MockMvcRequestBuilders.get("/orders/123/status"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("\"COMPLETED\""));
    }

}
