package br.com.zanelato.orderservice.dto;

public record OrderResponse(
        String orderId,
        String message
) {
}
