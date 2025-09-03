package br.com.zanelato.orderservice.repository;

import br.com.zanelato.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
