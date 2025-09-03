package br.com.zanelato.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    private int quantity;
    private Status status;
    private Instant createdAt;

    public Order() {
    }

    public Order(
            final String anId,
            final int aQuantity,
            final Status aStatus,
            final Instant aCreatedAt
    ) {
        this.orderId = anId;
        this.quantity = aQuantity;
        this.status = aStatus;
        this.createdAt = aCreatedAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public Order setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public Order setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Order setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Order setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

}
