package br.com.zanelato.orderservice.dto;

public record OrderRequest(
        String product,
        int quantity
) {
}
